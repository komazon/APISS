# NeoForge 26.1.2 / Minecraft 1.12.2 互換レイヤー開発手順書

## 概要

このドキュメントは、Minecraft 1.12.2用Forge ModをNeoForge 26.1.2上で動作させるための互換レイヤー（偽装レイヤー）を設計・実装するための手順書です。

互換レイヤーは、以下の3つの主要コンポーネントで構成されます。

1. Modディスカバリー＆ローダーハック（Discovery & Loader）
2. バイトコード・トランスフォーマー（ASM Bytecode Remapper）
3. 偽装API / ブリッジレイヤー（Bridge API）

---

## 1. システム全体像

### 1.1 Modディスカバリー＆ローダーハック

NeoForge標準のModローダーを騙し、1.12.2のJARを正規のModとして認識させる。

- `IModLocator` または NeoForgeのLanguage Providerを用いて 1.12.2Mod JARをスキャンする。
- 1.12.2 JARを独自クラスローダーで読み込み、バイトコードトランスフォーマーを通してクラスをロードする。

### 1.2 ASMバイトコード・リマッパー

1.12.2Modクラスをメモリ上で変換し、古いパッケージ名・型名・メソッド呼び出しを現代APIにリマップする。

- ASMの`ClassVisitor`/`MethodVisitor`/`ClassReader`/`ClassWriter`を使用する。
- メソッド呼び出し、フィールド参照、文字列定数中のパッケージパスを置換する。

### 1.3 偽装API／ブリッジレイヤー

1.12.2固有のFMLとMinecraft APIを模倣するダミークラス群を提供し、旧APIを現代NeoForge環境に変換する。

- 旧FMLイベントをNeoForgeの初期化イベントへブリッジする。
- 旧RegistryEvent呼び出しを遅延登録/DeferredRegister方式へ翻訳する。
- OpenGLベースの旧描画APIをPoseStack/VertexConsumerへ変換する。

---

## 2. 主要マッピング

1.12.2とNeoForge 26.1.2の主要パッケージ対応を整理します。

| 1.12.2 (MCP/SRG) | NeoForge 26.1.2 (Mojmap) | 備考 |
|---|---|---|
| `net.minecraft.block.*` | `net.minecraft.world.level.block.*` | ブロック関連 |
| `net.minecraft.item.*` | `net.minecraft.world.item.*` | アイテム関連 |
| `net.minecraft.world.*` | `net.minecraft.world.level.*` | `World`は`Level`へ変更 |
| `net.minecraft.util.math.*` | `net.minecraft.core.*` | `BlockPos`/`Vec3d`等、一部は`math`にも属する |
| `net.minecraft.util.ResourceLocation` | `net.minecraft.resources.ResourceLocation` | リソースID管理 |
| `net.minecraft.entity.player.*` | `net.minecraft.world.entity.player.*` | プレイヤーエンティティ |
| `net.minecraftforge.fml.*` | `net.neoforged.fml.*` | 置換せず偽装APIで吸収する方が安全 |

> 重要: `net.minecraftforge.fml.*`は直接変換せず、互換レイヤー内でダミーのパッケージ/クラスを提供する。

---

## 3. 実装フェーズ

以下の開発手順に従って段階的に進めます。

### Phase 1: MVP - 空の1.12.2ダミーModを読み込む

目的: NeoForge上で1.12.2Modがロードされ、最小限の初期化ログを出力すること。

手順:

1. NeoForgeのMod検出機構にフックし、1.12.2 JARを発見する。
- 1.12.2 Mod JARはWindowsでは`C:\Users\<USER_NAME>\AppData\Roaming\.minecraft\mods\modsapi\`に配置する。
2. 1.12.2 JARを専用ClassLoaderで読み込む。
3. `@Mod`アノテーションを持つクラスを検出し、最初のアクティベーションを行う。
4. 互換レイヤー自身に以下の旧FMLダミークラスを追加する。
   - `net.minecraftforge.fml.common.Mod`
   - `net.minecraftforge.fml.common.event.FMLPreInitializationEvent`
   - `net.minecraftforge.fml.common.event.FMLInitializationEvent`
   - `net.minecraftforge.fml.common.event.FMLPostInitializationEvent`
5. 旧Modの`@EventHandler`付きメソッドを呼び出してログを出す。

成功条件:

- NeoForge起動時に1.12.2Modが検出され、ロードされる。
- `FMLPreInitializationEvent`相当の処理が呼ばれ、ログが出る。

### Phase 2: 対応する旧FMLライフサイクルの偽装

目的: 旧FMLライフサイクルを現代NeoForgeイベントに接続する。

手順:

1. NeoForgeの`FMLCommonSetupEvent`等のモダンな初期化イベントにリスナを登録する。
2. 読み込んだ1.12.2Modクラスをリフレクションまたは`MethodHandles`で走査し、`@EventHandler`メソッドを収集する。
3. 収集したメソッドに対して、ダミーの旧イベントインスタンスを生成して順に呼び出す。
4. `FMLPreInitializationEvent`、`FMLInitializationEvent`、`FMLPostInitializationEvent`をそれぞれ適切なNeoForge初期化タイミングに割り当てる。

実装ポイント:

- ダミークラスのコンストラクタ/フィールドは旧イベントAPI互換を意識する。
- 呼び出し先の旧Modが期待するイベントコンテキストを最低限満たす。

### Phase 3: レジストリ翻訳と DeferredRegister ブリッジ

目的: 1.12.2の`RegistryEvent.Register<T>`による登録を、NeoForgeのモダン登録へ翻訳する。

手順:

1. 互換レイヤー内に以下のダミークラス/インターフェースを用意する。
   - `net.minecraftforge.event.RegistryEvent.Register<T>`
   - `net.minecraftforge.fml.common.registry.IForgeRegistry<T>`（または`IForgeRegistry`）
2. `IForgeRegistry.register(T entry)`を実装し、登録対象を即時登録せず内部キャッシュに保持する。
3. NeoForgeの`RegisterEvent`または遅延登録イベントが発火したタイミングで、内部キャッシュを抽出し実際のNeoForge登録APIへ流し込む。
4. 旧Modの`@SubscribeEvent`ハンドラを呼び出し、`event.getRegistry().register(...)`の挙動がキャッシュに蓄積されるようにする。

重要事項:

- 1.12.2の登録時点ではまだ現代のレジストリが完全に利用可能でない場合があるため、キャッシュを保存して後段でまとめて登録する。
- `event.getRegistry()`はダミー`IForgeRegistry`を返す。

### Phase 4: Block / Item のプロパティ変換

目的: 旧コンストラクタ呼び出しを現代のBlockBehaviour/Item.Propertiesに変換する。

手順:

1. ASMトランスフォーマーで`NEW`, `INVOKESPECIAL`の呼び出しを検知する。
2. 旧`net.minecraft.block.Block`や`net.minecraft.item.Item`のコンストラクタ呼び出しを、現代のファクトリメソッドまたはビルダーパターンへ書き換える。
   - 例: `new Block(Material.ROCK)` -> `new Block(BlockBehaviour.Properties.of(Material.STONE))` 相当
3. `BlockBridge`/`ItemBridge`ダミークラスを作成し、1.12.2の旧API呼び出しを受け止める。
4. `getStateFromMeta(int)` / `getMetaFromState(IBlockState)`は現代APIに存在しないため、ダミークラス側で16値のメタデータを`BlockState`プロパティへマッピングする状態管理を保持する。

実装ポイント:

- 旧`Material`や`SoundType`の情報を現代の`MaterialColor`/`BlockBehaviour.Properties`へ翻訳する。
- `setRegistryName`は旧形式の名前解決用に保持し、NeoForge側の`ResourceLocation`へ変換する。

### Phase 5: レンダリング互換レイヤー

目的: 1.12.2のGL 2.1 風描画コードを、現代のPoseStack/VertexConsumerベース描画へ翻訳する。

手順:

1. 旧描画APIのダミークラスを実装する。
   - `net.minecraft.client.renderer.GlStateManager`
   - `net.minecraft.client.renderer.Tessellator`
   - `net.minecraft.client.renderer.BufferBuilder`
2. `GlStateManager`呼び出しを現代の`PoseStack`操作にマッピングする。
   - `pushMatrix()` -> `PoseStack.pushPose()`
   - `translate(x, y, z)` -> `PoseStack.translate(x, y, z)`
   - `popMatrix()` -> `PoseStack.popPose()`
3. `Tessellator`/`BufferBuilder`の旧メソッドチェーンをフックし、内部で現代の`VertexConsumer`に値を詰め直す。
4. OpenGLステートの直接操作は最小化し、現代レンダリングパイプラインと互換するようにエミュレーションする。

注意:

- 旧レンダリング互換はクラッシュの温床になりやすいため、まずはテクスチャやモデルなしの簡易描画で動作確認する。

---

## 4. 互換レイヤー構造提案

### 4.1 パッケージ構造例

```
src/main/java/
  com/example/neoforge/compat/
    loader/
    asm/
    bridge/
    registry/
    render/
    event/
```

### 4.2 主要モジュール

- `loader`: 1.12.2 JARの検出と専用ClassLoader管理
- `asm`: ASMを使ったクラス変換ルール、マッピングテーブル、クラス/メソッド/フィールド書き換え
- `bridge`: 旧FML/Forge API互換ダミークラス
- `registry`: 旧RegistryEventのキャッシュとNeoForge登録変換
- `render`: GlStateManager/Tessellator系APIのエミュレーション
- `event`: 旧`@EventHandler`呼び出しと現代イベント橋渡し

---

## 5. 開発進め方（マイルストーン）

1. Phase 1: ロードと最小初期化の確認
2. Phase 2: 旧RegistryEventと無機能ブロックの登録
3. Phase 3: Item登録とテクスチャ/モデル変換
4. Phase 4: レンダリング互換とGUIブリッジ

### テスト戦略

- まずは最小の1.12.2ダミーModを用意し、ログ出力と`@Mod`初期化を確認する。
- 続いて、1つのブロックを旧`Register<Block>`経由で登録し、NeoForge上で存在するか確認する。
- 次に、簡易アイテム登録を追加し、ゲーム内に表示されるかを確認する。
- 最後に、旧レンダリング呼び出しを含む簡素なTile EntityやGUIを動かして安定性を確認する。

### 優先度の高い互換対象

1. `@Mod` / `@EventHandler` を通じたライフサイクル起動
2. `RegistryEvent.Register<Block>` / `RegistryEvent.Register<Item>` の変換
3. `Block` / `Item` のプロパティ変換
4. `ResourceLocation` と `setRegistryName` の変換
5. `GlStateManager` / `Tessellator` の互換レイヤー

---

## 6. 追加の注意点

- 旧Forge `net.minecraftforge.fml` 名前空間は、NeoForge内部で直接変換するよりも互換ダミーを提供したほうが副作用が少ない。
- ASMリマッピングは、文字列定数や`@Mod`注釈のクラス名にも適用する必要がある。
- 旧`World` / `IBlockState` などの型を現代の`Level` / `BlockState`にマッピングする処理は、互換レイヤー内で明確に分離する。
- 互換レイヤーの主要な境界は「旧Modコード」と「NeoForge実装コード」であり、境界ごとにテスト可能なAPIを設計する。

---

## 7. 参考例

- 旧Forgeの`@SubscribeEvent`登録とNeoForgeの現代イベントバスの違いをドキュメント化する。
- 旧`setRegistryName` / `ResourceLocation`の変換パターンを実装例としてまとめる。
- 旧描画APIの最小置き換えマップを表として整理する。

---

## 8. まとめ

この互換レイヤーは、
- 旧1.12.2Modの検出とロード
- ASMによるクラスパス・メソッドリマッピング
- 旧FMLライフサイクルの偽装
- 旧登録方式の NeoForge 変換
- 旧描画APIのブリッジ

を段階的に実装することで、NeoForge 26.1.2上で旧Modを動作させることを目指します。

まずは Phase 1 で最小のModロード動作を確立し、徐々に Phase 2～4 を拡張していくことが成功の鍵です。
