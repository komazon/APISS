package site.canva.my.komazonjapan.compat.asm;

import org.objectweb.asm.commons.Remapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 互換レイヤー: 1.12.2 の型名とメソッド名を現代 NeoForge 用にリマップする。
 *
 * ── 今回の追加内容（NoSuchMethodError 対策）──────────────────────────────
 *
 * 【エラーの原因】
 *   RTMCore.preInit() → RTMBlock.init() → BlockContainerCustomWithMeta(init)
 *   → (呼び出し) BlockState.func_177226_a(PropertyInteger, Comparable)
 *
 *   func_177226_a は 1.12.2 の難読化された IBlockState.withProperty(IProperty, Comparable)。
 *   現代 MC の BlockState には withProperty() も func_177226_a() も存在せず、
 *   対応するのは BlockState.setValue(Property, Comparable) である。
 *
 *   OpcodeFixVisitor が INVOKEINTERFACE → INVOKEVIRTUAL に直しても、
 *   呼び出し先のメソッド名が func_177226_a のままでは NoSuchMethodError になる。
 *   → METHOD_MAP に難読化名 → 現代名のマッピングを追加することで解決する。
 *
 * 【難読化名の対応表（1.12.2 IBlockState / BlockStateBase）】
 *   func_177226_a  → withProperty(IProperty, Comparable)    → BlockState.setValue(Property, T)
 *   func_177230_a  → getProperties()                        → BlockState.getValues()
 *   func_185900_c  → getBlock()                             → BlockState.getBlock()  ※Block返し
 *   func_177229_b  → getValue(IProperty)                    → BlockState.getValue(Property)
 *   func_185904_a  → is(Block)                              → BlockState.is(Block)
 *   func_177234_a  → withMirror / rotate / etc.            → 個別対応
 *
 * 【メソッドキーの形式】
 *   "owner#name descriptor"
 *   owner はリマップ後の内部名で登録する（ClassRemapper が先にオーナー名を変換するため）。
 *   ただし LegacyRemapper の mapMethodName() は ClassRemapper 内から呼ばれる際、
 *   owner がリマップ前か後かは ASM のバージョンと呼び出し順序に依存する。
 *   安全のためリマップ前・後の両方のキーを登録する。
 *
 * 【descriptor の扱い】
 *   1.12.2 バイトコード中の descriptor はリマップ前の型名を含む。
 *   mapMethodName() が呼ばれる時点では descriptor もリマップ済みになっている場合があるため、
 *   descriptor による絞り込みが難しいケースでは、名前だけでマッチさせる。
 *   ただし同名で引数が違う別メソッドとの衝突を防ぐため、可能な限り descriptor も指定する。
 */
public class LegacyRemapper extends Remapper {

    private static final Map<String, String> TYPE_MAP   = new LinkedHashMap<>();
    private static final Map<String, String> METHOD_MAP = new LinkedHashMap<>();

    /**
     * descriptor なしのメソッド名リマップ（名前のみで確定できるケース用）。
     * 衝突リスクが低い難読化名（func_XXXXXX_x 形式）に使う。
     */
    private static final Map<String, String> METHOD_NAME_ONLY_MAP = new LinkedHashMap<>();

    private static String methodKey(String owner, String name, String descriptor) {
        return owner + "#" + name + descriptor;
    }

    static {

        // ════════════════════════════════════════════════════════════════
        //  TYPE_MAP: クラス名・パッケージ名のリマップ
        // ════════════════════════════════════════════════════════════════

        // 現代のクラスに直接リマップして継承関係を維持させる
        TYPE_MAP.put("net/minecraft/entity/ai/EntityAIBase", "net/minecraft/world/entity/ai/Goal");
        TYPE_MAP.put("net/minecraft/entity/ai", "net/minecraft/world/entity/ai");
        TYPE_MAP.put("net/minecraft/world/World", "net/minecraft/world/level/Level");
        TYPE_MAP.put("net/minecraft/world/WorldServer", "net/minecraft/server/level/ServerLevel");

        TYPE_MAP.put("net/minecraft/block/state", "net/minecraftforge/compat/block/state");
        TYPE_MAP.put("net/minecraft/command", "net/minecraftforge/compat/command");
        TYPE_MAP.put("net/minecraft/creativetab", "net/minecraftforge/compat/creativetab");
        TYPE_MAP.put("net/minecraft/entity/player", "net/minecraftforge/compat/entity/player");
        TYPE_MAP.put("net/minecraft/init", "net/minecraftforge/compat/init");
        TYPE_MAP.put("net/minecraft/nbt", "net/minecraftforge/compat/nbt");
        TYPE_MAP.put("net/minecraft/tileentity", "net/minecraftforge/compat/tileentity");
        TYPE_MAP.put("net/minecraft/util/math/BlockPos", "net/minecraft/core/BlockPos");
        TYPE_MAP.put("net/minecraft/util/math/Vec3d", "net/minecraft/world/phys/Vec3");
        TYPE_MAP.put("net/minecraft/util/EnumFacing", "net/minecraftforge/compat/util/EnumFacing");
        TYPE_MAP.put("net/minecraft/util/EnumHand", "net/minecraftforge/compat/util/EnumHand");
        TYPE_MAP.put("net/minecraft/util/EnumActionResult", "net/minecraftforge/compat/util/EnumActionResult");
        TYPE_MAP.put("net/minecraft/util/AxisAlignedBB", "net/minecraftforge/compat/util/AxisAlignedBB");
        TYPE_MAP.put("net/minecraft/util/EnumParticleTypes", "net/minecraftforge/compat/util/EnumParticleTypes");
        TYPE_MAP.put("net/minecraft/util/ResourceLocation", "net/minecraftforge/compat/util/ResourceLocation");
        TYPE_MAP.put("net/minecraft/util/SoundEvent", "net/minecraftforge/compat/util/SoundEvent");
        TYPE_MAP.put("net/minecraft/util/SoundCategory", "net/minecraftforge/compat/util/SoundCategory");
        TYPE_MAP.put("net/minecraftforge/common/capabilities/Capability$IStorage", "site/canva/my/komazonjapan/compat/capabilities/DummyIStorage");
        TYPE_MAP.put("net/minecraftforge/common/capabilities/ICapabilityProvider", "site/canva/my/komazonjapan/compat/capabilities/DummyICapabilityProvider");
        TYPE_MAP.put("net/minecraft/potion/PotionEffect", "net/minecraftforge/compat/potion/PotionEffect");
        TYPE_MAP.put("net/minecraft/potion/Potion", "net/minecraftforge/compat/potion/Potion");
        TYPE_MAP.put("net/minecraft/launchwrapper/Launch", "net/minecraftforge/compat/launchwrapper/Launch");
        TYPE_MAP.put("net/minecraft/util/datafix/IFixableData", "net/minecraftforge/compat/util/datafix/IFixableData");
        TYPE_MAP.put("net/minecraft/util/datafix", "net/minecraftforge/compat/util/datafix");
        TYPE_MAP.put("net/minecraft/util/text/event", "net/minecraftforge/compat/util/text");
        TYPE_MAP.put("net/minecraft/util/text/translation", "net/minecraftforge/compat/util/text/translation");
        TYPE_MAP.put("net/minecraft/util/text", "net/minecraftforge/compat/util/text");
        TYPE_MAP.put("net/minecraft", "net/minecraftforge/compat");

        // ── 個別クラスの精密マッピング ──────────────────────────────────
        TYPE_MAP.put("net/minecraft/entity/player/EntityPlayer",    "net/minecraft/world/entity/player/Player");
        TYPE_MAP.put("net/minecraft/entity/player/EntityPlayerMP",  "net/minecraft/server/level/ServerPlayer");
        TYPE_MAP.put("net/minecraft/client/entity/EntityPlayerSP",  "net/minecraft/client/player/LocalPlayer");
        TYPE_MAP.put("net/minecraft/client/Minecraft",              "net/minecraftforge/compat/client/Minecraft");
        TYPE_MAP.put("net/minecraft/entity/EntityLivingBase",       "net/minecraft/world/entity/LivingEntity");
        TYPE_MAP.put("net/minecraft/entity/EntityCreature",         "net/minecraft/world/entity/PathfinderMob");
        TYPE_MAP.put("net/minecraft/entity/EntityAgeable",          "net/minecraft/world/entity/AgeableMob");

        TYPE_MAP.put("net/minecraft/block/state/IBlockState",       "net/minecraft/world/level/block/state/BlockState");
        TYPE_MAP.put("net/minecraft/world/WorldServer",             "net/minecraft/server/level/ServerLevel");
        TYPE_MAP.put("net/minecraft/world/WorldType",               "net/minecraft/world/level/WorldDataConfiguration");
        TYPE_MAP.put("net/minecraft/world/IBlockAccess",            "net/minecraft/world/level/BlockGetter");
        TYPE_MAP.put("net/minecraft/world/IWorldEventListener",     "net/minecraft/world/level/LevelEventListener");

        TYPE_MAP.put("net/minecraft/tileentity/TileEntity",         "net/minecraft/world/level/block/entity/BlockEntity");
        TYPE_MAP.put("net/minecraft/tileentity/TileEntityFurnace",  "net/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity");
        TYPE_MAP.put("net/minecraft/tileentity/TileEntityShulkerBox","net/minecraft/world/level/block/entity/ShulkerBoxBlockEntity");
        TYPE_MAP.put("net/minecraft/tileentity/TileEntityChest",    "net/minecraft/world/level/block/entity/ChestBlockEntity");
        TYPE_MAP.put("net/minecraft/tileentity/TileEntityHopper",   "net/minecraft/world/level/block/entity/HopperBlockEntity");

        TYPE_MAP.put("net/minecraft/nbt/NBTTagCompound",            "net/minecraft/nbt/CompoundTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagList",                "net/minecraft/nbt/ListTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagString",              "net/minecraft/nbt/StringTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagInt",                 "net/minecraft/nbt/IntTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagFloat",               "net/minecraft/nbt/FloatTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagDouble",              "net/minecraft/nbt/DoubleTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagByte",                "net/minecraft/nbt/ByteTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagLong",                "net/minecraft/nbt/LongTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagShort",               "net/minecraft/nbt/ShortTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagByteArray",           "net/minecraft/nbt/ByteArrayTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTTagIntArray",            "net/minecraft/nbt/IntArrayTag");
        TYPE_MAP.put("net/minecraft/nbt/NBTBase",                   "net/minecraft/nbt/Tag");

        TYPE_MAP.put("net/minecraft/util/math/BlockPos",            "net/minecraft/core/BlockPos");
        TYPE_MAP.put("net/minecraft/util/math/BlockPos$MutableBlockPos", "net/minecraft/core/BlockPos$MutableBlockPos");
        TYPE_MAP.put("net/minecraft/util/math/Vec3d",               "net/minecraft/world/phys/Vec3");
        TYPE_MAP.put("net/minecraft/util/math/AxisAlignedBB",       "net/minecraft/world/phys/AABB");
        TYPE_MAP.put("net/minecraft/util/math/ChunkPos",            "net/minecraft/world/level/ChunkPos");
        TYPE_MAP.put("net/minecraft/util/math/RayTraceResult",      "net/minecraft/world/phys/HitResult");
        TYPE_MAP.put("net/minecraft/util/text/ITextComponent",      "net/minecraft/network/chat/Component");
        TYPE_MAP.put("net/minecraft/util/text/TextComponentString", "net/minecraft/network/chat/Component");
        TYPE_MAP.put("net/minecraft/util/text/TextComponentTranslation", "net/minecraft/network/chat/Component");
        TYPE_MAP.put("net/minecraft/util/text/TextFormatting",      "net/minecraft/ChatFormatting");

        TYPE_MAP.put("net/minecraft/network/PacketBuffer",          "net/minecraft/network/FriendlyByteBuf");
        TYPE_MAP.put("net/minecraftforge/fml/common/network/simpleimpl/SimpleNetworkWrapper",
                     "net/minecraftforge/compat/network/SimpleNetworkWrapper");
        TYPE_MAP.put("net/minecraftforge/fml/common/network/simpleimpl/IMessage",
                     "net/minecraftforge/compat/network/IMessage");
        TYPE_MAP.put("net/minecraftforge/fml/common/network/simpleimpl/IMessageHandler",
                     "net/minecraftforge/compat/network/IMessageHandler");
        TYPE_MAP.put("net/minecraftforge/fml/common/network/simpleimpl/MessageContext",
                     "net/minecraftforge/compat/network/MessageContext");

        TYPE_MAP.put("net/minecraft/item/ItemStack",                "net/minecraft/world/item/ItemStack");

        TYPE_MAP.put("net/minecraftforge/fml/common/registry/ForgeRegistries",
                     "net/minecraftforge/registries/ForgeRegistries");
        TYPE_MAP.put("net/minecraftforge/fml/common/registry/GameRegistry",
                     "net/minecraftforge/compat/fml/common/registry/GameRegistry");
        TYPE_MAP.put("net/minecraftforge/fml/common/registry/EntityRegistry",
                     "net/minecraftforge/compat/fml/common/registry/EntityRegistry");

        TYPE_MAP.put("net/minecraft/block/properties/PropertyInteger",
                     "net/minecraft/world/level/block/state/properties/IntegerProperty");
        TYPE_MAP.put("net/minecraft/block/properties/PropertyBool",
                     "net/minecraft/world/level/block/state/properties/BooleanProperty");
        TYPE_MAP.put("net/minecraft/block/properties/PropertyEnum",
                     "net/minecraft/world/level/block/state/properties/EnumProperty");
        TYPE_MAP.put("net/minecraft/block/properties/PropertyHelper",
                     "net/minecraft/world/level/block/state/properties/IntegerProperty");
        TYPE_MAP.put("net/minecraft/block/properties/IProperty",
                     "net/minecraft/world/level/block/state/properties/Property");

        TYPE_MAP.put("net/minecraft/block/state/BlockStateContainer",
                     "net/minecraftforge/compat/block/state/BlockStateContainer");

        TYPE_MAP.put("net/minecraft/block/Block", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/ITileEntityProvider", "net/minecraftforge/compat/block/ITileEntityProvider");
        TYPE_MAP.put("net/minecraft/block/BlockContainer", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockStairs", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockSlab", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockPane", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockFence", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockFenceGate", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockDoor", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/block/BlockPressurePlate", "net/minecraftforge/compat/block/LegacyBlock");
        TYPE_MAP.put("net/minecraft/item/ItemDoor", "net/minecraftforge/compat/item/LegacyItemDoor");
        TYPE_MAP.put("net/minecraft/item/Item", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemBlock", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemSword", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemPickaxe", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemAxe", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemSpade", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemHoe", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemFood", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemArmor", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/item/ItemBow", "net/minecraftforge/compat/item/LegacyItem");
        TYPE_MAP.put("net/minecraft/inventory/IInventory", "net/minecraftforge/compat/inventory/IInventory");
        TYPE_MAP.put("net/minecraft/block/material/Material", "net/minecraftforge/compat/block/LegacyMaterial");
        TYPE_MAP.put("net/minecraft/entity/player/EntityPlayer", "net/minecraft/world/entity/player/Player");
        TYPE_MAP.put("net/minecraft/entity/Entity", "net/minecraft/world/entity/Entity");
        TYPE_MAP.put("net/minecraft/entity/EntityLivingBase", "net/minecraft/world/entity/LivingEntity");
        TYPE_MAP.put("net/minecraft/entity/monster/EntityMob", "net/minecraft/world/entity/LivingEntity");

        TYPE_MAP.put("net/minecraft/world/World", "net/minecraft/world/level/Level");
        TYPE_MAP.put("net/minecraft/world/WorldServer", "net/minecraft/server/level/ServerLevel");
        TYPE_MAP.put("net/minecraft/world/chunk/Chunk", "net/minecraft/world/level/chunk/LevelChunk");
        TYPE_MAP.put("net/minecraft/world/storage/loot/LootEntry", "net/minecraft/world/level/storage/loot/entries/LootPoolEntry");
        TYPE_MAP.put("net/minecraft/world/storage/loot", "net/minecraft/world/level/storage/loot/entries");

        TYPE_MAP.put("net/minecraft/block", "net/minecraftforge/compat/block");
        TYPE_MAP.put("net/minecraft/item", "net/minecraftforge/compat/item");
        TYPE_MAP.put("net/minecraft/block/material", "net/minecraftforge/compat/block/material");
        TYPE_MAP.put("net/minecraft/world/chunk", "net/minecraftforge/compat/world/chunk");
        TYPE_MAP.put("net/minecraft/util/text", "net/minecraftforge/compat/util/text");
        TYPE_MAP.put("net/minecraft/client/renderer/GlStateManager", "net/minecraftforge/compat/renderer/GlStateManager");
        TYPE_MAP.put("net/minecraft/client/renderer/Tessellator", "net/minecraftforge/compat/renderer/Tessellator");
        TYPE_MAP.put("net/minecraft/client/renderer/BufferBuilder", "net/minecraftforge/compat/renderer/BufferBuilder");
        TYPE_MAP.put("net/minecraft/client/renderer/vertex/VertexFormat", "net/minecraftforge/compat/renderer/vertex/VertexFormat");
        TYPE_MAP.put("net/minecraft/client/renderer/vertex/DefaultVertexFormats", "net/minecraftforge/compat/renderer/vertex/DefaultVertexFormats");
        TYPE_MAP.put("net/minecraft/client/renderer", "net/minecraftforge/compat/renderer");
        TYPE_MAP.put("net/minecraft/client/renderer/vertex", "net/minecraftforge/compat/renderer/vertex");

        TYPE_MAP.put("net/minecraft/block/properties",          "net/minecraft/world/level/block/state/properties");
        TYPE_MAP.put("net/minecraft/block/state/pattern",       "net/minecraft/world/level/block/state/pattern");

        TYPE_MAP.put("net/minecraft/entity/ai/attributes",      "net/minecraft/world/entity/ai/attributes");
        TYPE_MAP.put("net/minecraft/entity/item",               "net/minecraft/world/entity/item");
        TYPE_MAP.put("net/minecraft/entity/monster",            "net/minecraft/world/entity/monster");
        TYPE_MAP.put("net/minecraft/entity/projectile",         "net/minecraft/world/entity/projectile");
        TYPE_MAP.put("net/minecraft/entity/EntityLiving",       "net/minecraft/world/entity/LivingEntity");

        TYPE_MAP.put("net/minecraft/item/crafting",             "net/minecraft/world/item/crafting");
        TYPE_MAP.put("net/minecraft/network/datasync",          "net/minecraft/network/syncher");
        TYPE_MAP.put("net/minecraft/network/play/client",       "net/minecraft/network/protocol/game");
        TYPE_MAP.put("net/minecraft/network/PacketBuffer",      "net/minecraft/network/FriendlyByteBuf");

        TYPE_MAP.put("net/minecraft/world/biome",               "net/minecraft/world/level/biome");
        TYPE_MAP.put("net/minecraft/world/gen/feature",         "net/minecraft/world/level/levelgen/feature");
        TYPE_MAP.put("net/minecraft/world/gen",                 "net/minecraft/world/level/levelgen");
        TYPE_MAP.put("net/minecraft/world/storage",             "net/minecraft/world/level/storage");

        TYPE_MAP.put("net/minecraft/server/management",         "net/minecraft/server/players");
        TYPE_MAP.put("net/minecraft/server/integrated",         "net/minecraft/client/server");
        TYPE_MAP.put("net/minecraft/server/MinecraftServer",    "net/minecraft/server/MinecraftServer");

        TYPE_MAP.put("net/minecraft/util/registry",             "net/minecraft/core/registries");
        TYPE_MAP.put("net/minecraft/util/math/ChunkPos",        "net/minecraft/world/level/ChunkPos");
        TYPE_MAP.put("net/minecraft/util/math/MathHelper",      "net/minecraft/util/Mth");
        TYPE_MAP.put("net/minecraft/profiler",                  "net/minecraft/util/profiling");
        TYPE_MAP.put("net/minecraft/stats",                     "net/minecraft/stats");
        TYPE_MAP.put("net/minecraft/enchantment",               "net/minecraft/world/item/enchantment");
        TYPE_MAP.put("net/minecraft/pathfinding",               "net/minecraft/world/entity/ai/navigation");
        TYPE_MAP.put("net/minecraft/dispenser",                 "net/minecraft/core/dispenser");
        TYPE_MAP.put("net/minecraft/client/audio",              "net/minecraft/client/sounds");
        TYPE_MAP.put("net/minecraft/client/particle",           "net/minecraft/client/particle");
        TYPE_MAP.put("net/minecraft/client/util",               "net/minecraft/client/util");
        TYPE_MAP.put("net/minecraft/client/settings",           "net/minecraft/client");
        TYPE_MAP.put("net/minecraft/client/network",            "net/minecraft/client/multiplayer");
        TYPE_MAP.put("net/minecraft/client/entity",             "net/minecraft/client/player");
        TYPE_MAP.put("net/minecraft/client/multiplayer/WorldClient", "net/minecraft/client/multiplayer/ClientLevel");
        TYPE_MAP.put("net/minecraft/client/renderer/block/model",    "net/minecraft/client/resources/model");
        TYPE_MAP.put("net/minecraft/client/renderer/block/statemap", "net/minecraft/client/renderer");
        TYPE_MAP.put("net/minecraft/client/renderer/color",     "net/minecraft/client/color");
        TYPE_MAP.put("net/minecraft/client/renderer/texture",   "net/minecraft/client/renderer/texture");
        TYPE_MAP.put("net/minecraft/client/renderer/tileentity","net/minecraft/client/renderer/blockentity");
        TYPE_MAP.put("net/minecraft/client/renderer/entity",    "net/minecraft/client/renderer/entity");
        TYPE_MAP.put("net/minecraft/client/resources",          "net/minecraft/client/resources");

        TYPE_MAP.put("net/minecraft/client/gui/inventory",      "net/minecraft/client/gui/screens/inventory");
        TYPE_MAP.put("net/minecraft/client/gui/GuiScreen",      "net/minecraftforge/compat/client/gui/LegacyGuiScreen");
        TYPE_MAP.put("net/minecraft/client/gui/GuiButton",      "net/minecraft/client/gui/components/Button");
        TYPE_MAP.put("net/minecraft/client/gui",                "net/minecraft/client/gui/screens");
        TYPE_MAP.put("net/minecraft/inventory",                 "net/minecraft/world/inventory");
        TYPE_MAP.put("net/minecraft/client/model",              "net/minecraft/client/model");

        TYPE_MAP.put("net/minecraftforge/energy",               "net/minecraftforge/compat/energy");
        TYPE_MAP.put("net/minecraftforge/fluids/capability/templates", "net/minecraftforge/compat/fluids/capability/templates");
        TYPE_MAP.put("net/minecraftforge/fluids/capability",    "net/minecraftforge/compat/fluids/capability");
        TYPE_MAP.put("net/minecraftforge/fluids",               "net/minecraftforge/compat/fluids");
        TYPE_MAP.put("net/minecraftforge/items/wrapper",        "net/minecraftforge/compat/items/wrapper");
        TYPE_MAP.put("net/minecraftforge/items",                "net/minecraftforge/compat/items");
        TYPE_MAP.put("net/minecraftforge/oredict",              "net/minecraftforge/compat/oredict");

        TYPE_MAP.put("net/minecraftforge/client/event/sound",   "net/minecraftforge/compat/client/event/sound");
        TYPE_MAP.put("net/minecraftforge/client/event",         "net/minecraftforge/compat/client/event");
        TYPE_MAP.put("net/minecraftforge/client/model/obj",     "net/minecraftforge/compat/client/model/obj");
        TYPE_MAP.put("net/minecraftforge/client/model/pipeline","net/minecraftforge/compat/client/model/pipeline");
        TYPE_MAP.put("net/minecraftforge/client/model",         "net/minecraftforge/compat/client/model");
        TYPE_MAP.put("net/minecraftforge/client",               "net/minecraftforge/compat/client");

        TYPE_MAP.put("net/minecraftforge/fml/client/config",    "net/minecraftforge/compat/fml/client/config");
        TYPE_MAP.put("net/minecraftforge/fml/client/event",     "net/minecraftforge/compat/fml/client/event");
        TYPE_MAP.put("net/minecraftforge/fml/client/registry",  "net/minecraftforge/compat/fml/client/registry");
        TYPE_MAP.put("net/minecraftforge/fml/client",           "net/minecraftforge/compat/fml/client");

        TYPE_MAP.put("net/minecraftforge/fml/common/gameevent", "net/minecraftforge/compat/fml/common/gameevent");

        TYPE_MAP.put("net/minecraftforge/event/entity/living",  "net/minecraftforge/compat/event/entity/living");
        TYPE_MAP.put("net/minecraftforge/event/entity/player",  "net/minecraftforge/compat/event/entity/player");
        TYPE_MAP.put("net/minecraftforge/event/entity",         "net/minecraftforge/compat/event/entity");
        TYPE_MAP.put("net/minecraftforge/event/world",          "net/minecraftforge/compat/event/world");
        TYPE_MAP.put("net/minecraftforge/event/block",          "net/minecraftforge/compat/event/block");
        TYPE_MAP.put("net/minecraftforge/event/AttachCapabilitiesEvent", "net/minecraftforge/compat/event/AttachCapabilitiesEvent");

        TYPE_MAP.put("net/minecraftforge/common/crafting",      "net/minecraftforge/compat/common/crafting");
        TYPE_MAP.put("net/minecraftforge/common/model",         "net/minecraftforge/compat/common/model");
        TYPE_MAP.put("net/minecraftforge/common/property",      "net/minecraftforge/compat/common/property");
        TYPE_MAP.put("net/minecraftforge/common/util",          "net/minecraftforge/compat/common/util");
        TYPE_MAP.put("net/minecraftforge/server/command",       "net/minecraftforge/compat/server/command");


        // ════════════════════════════════════════════════════════════════
        //  METHOD_MAP: 難読化メソッド名（owner + name + descriptor → 現代名）
        //
        //  owner はリマップ後の内部名で登録する。
        //  安全のためリマップ前の owner でも同じエントリを追加する。
        // ════════════════════════════════════════════════════════════════

        // ── NonNullList ──────────────────────────────────────────────────
        METHOD_MAP.put(methodKey("net/minecraft/util/NonNullList",        "func_191196_a", "()Lnet/minecraft/util/NonNullList;"),        "create");
        METHOD_MAP.put(methodKey("net/minecraft/core/NonNullList",        "func_191196_a", "()Lnet/minecraft/core/NonNullList;"),        "create");
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/util/NonNullList", "func_191196_a", "()Lnet/minecraftforge/compat/util/NonNullList;"), "create");

        // ── Property 静的ファクトリ（RTM/NGTLib 対策） ─────────────────────
        //
        //  問題: RTM の BlockContainerCustomWithMeta.<clinit> が
        //        PropertyInteger.func_177719_a(String, int, int) を INVOKESTATIC する。
        //        TYPE_MAP が net/minecraft/block/properties/PropertyInteger
        //                  → net/minecraft/world/level/block/state/properties/IntegerProperty
        //        にリマップした後、IntegerProperty に func_177719_a が存在しないため
        //        NoSuchMethodError になる。
        //        → post-remap オーナーで "func_177719_a" → "create" を登録して解決。
        //        安全策として pre-remap オーナーでも同じエントリを追加する。
        //
        //  PropertyInteger.func_177719_a(String, min, max) → IntegerProperty.create
        METHOD_MAP.put(methodKey("net/minecraft/world/level/block/state/properties/IntegerProperty",
                "func_177719_a",
                "(Ljava/lang/String;II)Lnet/minecraft/world/level/block/state/properties/IntegerProperty;"),
                "create");
        METHOD_MAP.put(methodKey("net/minecraft/block/properties/PropertyInteger",
                "func_177719_a",
                "(Ljava/lang/String;II)Lnet/minecraft/block/properties/PropertyInteger;"),
                "create");
        //  PropertyBool.func_177716_a(String) → BooleanProperty.create
        METHOD_MAP.put(methodKey("net/minecraft/world/level/block/state/properties/BooleanProperty",
                "func_177716_a",
                "(Ljava/lang/String;)Lnet/minecraft/world/level/block/state/properties/BooleanProperty;"),
                "create");
        METHOD_MAP.put(methodKey("net/minecraft/block/properties/PropertyBool",
                "func_177716_a",
                "(Ljava/lang/String;)Lnet/minecraft/block/properties/PropertyBool;"),
                "create");
        //  PropertyEnum.func_177709_a(String, Class) → EnumProperty.create
        METHOD_MAP.put(methodKey("net/minecraft/world/level/block/state/properties/EnumProperty",
                "func_177709_a",
                "(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/world/level/block/state/properties/EnumProperty;"),
                "create");
        METHOD_MAP.put(methodKey("net/minecraft/block/properties/PropertyEnum",
                "func_177709_a",
                "(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/block/properties/PropertyEnum;"),
                "create");

        // ── BlockState / IBlockState の難読化メソッド（RTM 対策）──────────
        //
        //  1.12.2 難読化名         現代 MC のメソッド名
        //  func_177226_a         → setValue(Property, T)          ※withProperty 相当
        //  func_177229_b         → getValue(Property)             ※getValue 相当
        //  func_177230_a         → getValues()                    ※getProperties 相当
        //  func_185900_c         → getBlock()
        //  func_185904_a         → is(Block)
        //  func_177234_a / _b    → rotate / mirror               ※BlockState.rotate / mirror
        //  func_185907_a         → getActualState()              ※ない場合は this 返し で対処
        //
        //  オーナーはリマップ前（IBlockState）とリマップ後（BlockState）の両方を登録。
        //  descriptor はリマップ前・後で変わるため、名前のみで対応する
        //  METHOD_NAME_ONLY_MAP に登録する（下記参照）。
        //
        //  descriptor 付きエントリは「完全一致が取れる場合だけ」追加する。

        // リマップ後オーナー: net/minecraft/world/level/block/state/BlockState
        //   withProperty(IProperty, Comparable) → setValue(Property, T)
        //   ※descriptor はリマップ後: (Lnet/minecraftforge/compat/block/properties/PropertyInteger;Ljava/lang/Comparable;)Lnet/minecraft/world/level/block/state/BlockState;
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_177226_a",
                "(Lnet/minecraftforge/compat/block/properties/PropertyInteger;Ljava/lang/Comparable;)Lnet/minecraft/world/level/block/state/BlockState;"),
            "setValue");
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_177226_a",
                "(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Lnet/minecraft/world/level/block/state/BlockState;"),
            "setValue");

        //   getValue(IProperty) → getValue(Property)
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_177229_b",
                "(Lnet/minecraftforge/compat/block/properties/PropertyInteger;)Ljava/lang/Comparable;"),
            "getValue");
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_177229_b",
                "(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"),
            "getValue");

        //   getProperties() → getValues()
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_177230_a",
                "()Ljava/util/Collection;"),
            "getValues");

        //   getBlock() → getBlock()（名前は同じだが念のため）
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_185900_c",
                "()Lnet/minecraft/world/level/block/Block;"),
            "getBlock");
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_185900_c",
                "()Lnet/minecraftforge/compat/block/LegacyBlock;"),
            "getBlock");

        //   is(Block) → is(Block)
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_185904_a",
                "(Lnet/minecraft/world/level/block/Block;)Z"),
            "is");
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_185904_a",
                "(Lnet/minecraftforge/compat/block/LegacyBlock;)Z"),
            "is");

        //   getActualState(IBlockAccess, BlockPos) → compat では this を返すだけ（スタブ）
        //   compat の BlockState スタブに getActualState() があれば呼ぶ
        METHOD_MAP.put(
            methodKey("net/minecraft/world/level/block/state/BlockState",
                "func_185907_a",
                "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            "getActualState");

        // リマップ前オーナー（LegacyRemapper が map() する前に呼ばれるケース用）
        //   IBlockState はリマップ後 BlockState になるが、mapMethodName の owner が
        //   リマップ前のままで呼ばれることもあるため両方登録する。
        METHOD_MAP.put(
            methodKey("net/minecraft/block/state/IBlockState",
                "func_177226_a",
                "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;"),
            "setValue");
        METHOD_MAP.put(
            methodKey("net/minecraft/block/state/IBlockState",
                "func_177229_b",
                "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;"),
            "getValue");
        METHOD_MAP.put(
            methodKey("net/minecraft/block/state/IBlockState",
                "func_177230_a",
                "()Ljava/util/Collection;"),
            "getValues");
        METHOD_MAP.put(
            methodKey("net/minecraft/block/state/IBlockState",
                "func_185900_c",
                "()Lnet/minecraft/block/Block;"),
            "getBlock");
        METHOD_MAP.put(
            methodKey("net/minecraft/block/state/IBlockState",
                "func_185904_a",
                "(Lnet/minecraft/block/Block;)Z"),
            "is");
        METHOD_MAP.put(
            methodKey("net/minecraft/block/state/IBlockState",
                "func_185907_a",
                "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"),
            "getActualState");

        // ── Block 系の難読化名（LegacyBlock compat スタブに移譲） ──────────
        // func_176223_P → getDefaultState()
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/block/LegacyBlock", "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;"), "getDefaultState");
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/block/LegacyBlock", "func_176223_P", "()Lnet/minecraft/world/level/block/state/BlockState;"), "getDefaultState");

        // func_149663_c → setBlockName (setUnlocalizedName 相当)
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/block/LegacyBlock", "func_149663_c", "(Ljava/lang/String;)Lnet/minecraft/block/Block;"), "setBlockName");
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/block/LegacyBlock", "func_149663_c", "(Ljava/lang/String;)Lnet/minecraftforge/compat/block/LegacyBlock;"), "setBlockName");

        // func_180632_j(IBlockAccess, BlockPos) → getActualState (compat スタブに委譲)
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/block/LegacyBlock",
                "func_180632_j",
                "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"),
            "getActualState");
        METHOD_MAP.put(methodKey("net/minecraftforge/compat/block/LegacyBlock",
                "func_180632_j",
                "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            "getActualState");

        // ── Entity 系（既存 + 追加） ──────────────────────────────────────
        // setCustomNameTag → setCustomName compat stub
        METHOD_MAP.put(methodKey("net/minecraft/world/entity/Entity", "func_96094_a", "(Ljava/lang/String;)V"), "setCustomNameTag");
        METHOD_MAP.put(methodKey("net/minecraft/world/entity/Entity", "func_95999_t", "()Ljava/lang/String;"), "getCustomNameTag");


        // ════════════════════════════════════════════════════════════════
        //  METHOD_NAME_ONLY_MAP: 名前だけでマッチ（descriptor が変動するケース）
        //
        //  func_XXXXXX_x 形式の難読化名は衝突リスクが極めて低いため、
        //  descriptor なしでリマップしても安全。
        //  METHOD_MAP で拾えなかった場合のフォールバックとして使う。
        // ════════════════════════════════════════════════════════════════

        // BlockState 系
        METHOD_NAME_ONLY_MAP.put("func_177226_a", "setValue");        // withProperty / setValue
        METHOD_NAME_ONLY_MAP.put("func_177229_b", "getValue");        // getValue
        METHOD_NAME_ONLY_MAP.put("func_177230_a", "getValues");       // getProperties → getValues
        METHOD_NAME_ONLY_MAP.put("func_185900_c", "getBlock");        // getBlock
        METHOD_NAME_ONLY_MAP.put("func_185904_a", "is");              // is(Block)
        METHOD_NAME_ONLY_MAP.put("func_185907_a", "getActualState");  // getActualState

        // Block 系
        METHOD_NAME_ONLY_MAP.put("func_176223_P", "getDefaultState"); // getDefaultState
        METHOD_NAME_ONLY_MAP.put("func_149663_c", "setBlockName");    // setUnlocalizedName → setBlockName
        METHOD_NAME_ONLY_MAP.put("func_180632_j", "getActualState");  // getActualState(world, pos)
        METHOD_NAME_ONLY_MAP.put("func_149749_a", "setHardness");     // setHardness(float)
        METHOD_NAME_ONLY_MAP.put("func_149752_b", "setResistance");   // setResistance(float)
        METHOD_NAME_ONLY_MAP.put("func_149715_a", "setSoundType");    // setStepSound → setSoundType
        METHOD_NAME_ONLY_MAP.put("func_149711_c", "setLightLevel");   // setLightLevel(float)
        METHOD_NAME_ONLY_MAP.put("func_149713_g", "setLightOpacity"); // setLightOpacity(int)

        // Entity 系
        METHOD_NAME_ONLY_MAP.put("func_70105_a", "setSize");          // setSize(width, height)
        METHOD_NAME_ONLY_MAP.put("func_96094_a", "setCustomNameTag"); // setCustomNameTag
        METHOD_NAME_ONLY_MAP.put("func_95999_t", "getCustomNameTag"); // getCustomNameTag

        // Minecraft 系 (Client)
        // func_71410_x() は static メソッドなので、リマップ先も static メソッド名にする
        METHOD_NAME_ONLY_MAP.put("func_71410_x", "func_71410_x");     // getInstance() → 互換ラッパーの static メソッド
        METHOD_NAME_ONLY_MAP.put("func_71401_C", "func_71401_C");     // level の取得
        METHOD_NAME_ONLY_MAP.put("func_175606_aa", "func_175606_aa"); // player の取得
        METHOD_NAME_ONLY_MAP.put("func_71411_J", "func_71411_J");     // timer の取得
        METHOD_NAME_ONLY_MAP.put("func_71398_f", "func_71398_f");     // addScheduledTask(Runnable)
        METHOD_NAME_ONLY_MAP.put("func_147112_ai", "func_147112_ai"); // isIntegratedServerRunning()
        METHOD_NAME_ONLY_MAP.put("func_71355_Q", "func_71355_Q");     // isSingleplayer()
        METHOD_NAME_ONLY_MAP.put("func_71353_P", "func_71353_P");     // launchIntegratedServer()
        METHOD_NAME_ONLY_MAP.put("func_71356_B", "func_71356_B");     // isDemo()
        METHOD_NAME_ONLY_MAP.put("func_71369_N", "func_71369_N");     // getDebugProfilerLocation()
        METHOD_NAME_ONLY_MAP.put("func_71371_a", "func_71371_a");     // launchSurvivalIntegratedServer(String, String)
        METHOD_NAME_ONLY_MAP.put("func_71372_g", "func_71372_g");     // displayGuiScreen(GuiScreen)
        METHOD_NAME_ONLY_MAP.put("func_71375_t", "func_71375_t");     // isFancyGraphics()
        METHOD_NAME_ONLY_MAP.put("func_71386_H", "func_71386_H");     // getDebugFPS()
        METHOD_NAME_ONLY_MAP.put("func_71387_A", "func_71387_A");     // integratedServerIsRunning()
        METHOD_NAME_ONLY_MAP.put("func_71389_G", "func_71389_G");     // startTimerHackThread()
        METHOD_NAME_ONLY_MAP.put("func_71396_d", "func_71396_d");     // addGraphicsOrWorldToCrashReport(CrashReport)
        METHOD_NAME_ONLY_MAP.put("func_71398_f", "func_71398_f");     // addScheduledTask(Callable)
        METHOD_NAME_ONLY_MAP.put("func_71400_L", "func_71400_L");     // shutdown()
        METHOD_NAME_ONLY_MAP.put("func_71403_a", "func_71403_a");     // loadWorld(WorldClient)
        METHOD_NAME_ONLY_MAP.put("func_71404_a", "func_71404_a");     // crashReportAdded(CrashReport)
        METHOD_NAME_ONLY_MAP.put("func_71405_e", "func_71405_e");     // shutdownMinecraftApplet()
        METHOD_NAME_ONLY_MAP.put("func_71407_l", "func_71407_l");     // runTick()
        METHOD_NAME_ONLY_MAP.put("func_71410_x", "func_71410_x");     // getInstance()
        METHOD_NAME_ONLY_MAP.put("func_71411_J", "func_71411_J");     // runGameLoop()
        METHOD_NAME_ONLY_MAP.put("func_71413_O", "func_71413_O");     // getLaunchedVersion()
        METHOD_NAME_ONLY_MAP.put("func_71414_Y", "func_71414_Y");     // getDefaultResourcePack()
        METHOD_NAME_ONLY_MAP.put("func_71415_M", "func_71415_M");     // checkGLError()
        METHOD_NAME_ONLY_MAP.put("func_71417_b", "func_71417_b");     // isInGameHasFocus()
        METHOD_NAME_ONLY_MAP.put("func_71418_a", "func_71418_a");     // addResourcePack(ResourcePack)
        METHOD_NAME_ONLY_MAP.put("func_71419_M", "func_71419_M");     // updateDisplay()
        METHOD_NAME_ONLY_MAP.put("func_71420_Lb", "func_71420_Lb");   // getRenderEntity()
        METHOD_NAME_ONLY_MAP.put("func_71421_U", "func_71421_U");     // getMouseOver(float)
        METHOD_NAME_ONLY_MAP.put("func_71422_O", "func_71422_O");     // connectToIntegratedServer()
        METHOD_NAME_ONLY_MAP.put("func_71423_a", "func_71423_a");     // loadWorld(WorldClient, String, IProgressUpdate)
        METHOD_NAME_ONLY_MAP.put("func_71424_i", "func_71424_i");     // setDimensionAndSpawnPlayer(int)
        METHOD_NAME_ONLY_MAP.put("func_71425_a", "func_71425_a");     // removeResourcePack(String)
        METHOD_NAME_ONLY_MAP.put("func_71426_a", "func_71426_a");     // draw(boolean, int)
        METHOD_NAME_ONLY_MAP.put("func_71427_K", "func_71427_K");     // updateFramebufferSize()
        METHOD_NAME_ONLY_MAP.put("func_71428_T", "func_71428_T");     // getNetworkManager()
        METHOD_NAME_ONLY_MAP.put("func_71429_W", "func_71429_W");     // getWindowWidth()
        METHOD_NAME_ONLY_MAP.put("func_71430_J", "func_71430_J");     // updateWindowProperties()
        METHOD_NAME_ONLY_MAP.put("func_71431_p", "func_71431_p");     // setErrorDescription(String)
        METHOD_NAME_ONLY_MAP.put("func_71432_b", "func_71432_b");     // resizeDisplayMode(int, int, boolean, boolean)
        METHOD_NAME_ONLY_MAP.put("func_71433_a", "func_71433_a");     // sendClickBlockToController(PlayerEntity, BlockPos, EnumFacing)
        METHOD_NAME_ONLY_MAP.put("func_71434_d", "func_71434_d");     // setServerData(ServerData)
        METHOD_NAME_ONLY_MAP.put("func_71435_a", "func_71435_a");     // clickBlock(BlockPos, EnumFacing)
        METHOD_NAME_ONLY_MAP.put("func_71436_j", "func_71436_j");     // triggerFogSettingsUpdate()
        METHOD_NAME_ONLY_MAP.put("func_71437_b", "func_71437_b");     // displayDebugInfo(DebugGUIProfiler)
        METHOD_NAME_ONLY_MAP.put("func_71438_a", "func_71438_a");     // destroyBlock(BlockPos, boolean)
        METHOD_NAME_ONLY_MAP.put("func_71439_a", "func_71439_a");     // attackLeftClick()
        METHOD_NAME_ONLY_MAP.put("func_71440_d", "func_71440_d");     // refreshResources()
        METHOD_NAME_ONLY_MAP.put("func_71441_e", "func_71441_e");     // setConnectedToRealms(boolean)
        METHOD_NAME_ONLY_MAP.put("func_71442_a", "func_71442_a");     // rightClickMouse()
        METHOD_NAME_ONLY_MAP.put("func_71443_c", "func_71443_c");     // updateKeyBindings()
        METHOD_NAME_ONLY_MAP.put("func_71444_e", "func_71444_e");     // registerResourceReloadListener(IResourceManagerReloadListener)
        METHOD_NAME_ONLY_MAP.put("func_71445_f", "func_71445_f");     // middleClickMouse()
        METHOD_NAME_ONLY_MAP.put("func_71446_a", "func_71446_a");     // addFrameTimerLog(String, long)
        METHOD_NAME_ONLY_MAP.put("func_71447_a", "func_71447_a");     // updateDisplayMode(Map)
        METHOD_NAME_ONLY_MAP.put("func_71448_a", "func_71448_a");     // displayIngameMessage(ITextComponent)
        METHOD_NAME_ONLY_MAP.put("func_71449_a", "func_71449_a");     // renderStreamOverlay(String, int, int, int, int)
        METHOD_NAME_ONLY_MAP.put("func_71450_d", "func_71450_d");     // toggleFullscreen()
        METHOD_NAME_ONLY_MAP.put("func_71451_h", "func_71451_h");     // processKeyBinds()
        METHOD_NAME_ONLY_MAP.put("func_71452_e", "func_71452_e");     // deleteDirectory(File)
        METHOD_NAME_ONLY_MAP.put("func_71453_q", "func_71453_q");     // displayCrashReport(CrashReport)
        METHOD_NAME_ONLY_MAP.put("func_71455_a", "func_71455_a");     // scheduleMemoryReporter()
        METHOD_NAME_ONLY_MAP.put("func_71456_a", "func_71456_a");     // refreshStreamStatus()
        METHOD_NAME_ONLY_MAP.put("func_71457_a", "func_71457_a");     // showDemoHelpScreen()
        METHOD_NAME_ONLY_MAP.put("func_71458_a", "func_71458_a");     // processKeyF3()
        METHOD_NAME_ONLY_MAP.put("func_71459_n", "func_71459_n");     // runTickKeyboard()
        METHOD_NAME_ONLY_MAP.put("func_71460_u", "func_71460_u");     // getRenderGlobal()
        METHOD_NAME_ONLY_MAP.put("func_71461_d", "func_71461_d");     // resetIntegratedServerSnooper()
        METHOD_NAME_ONLY_MAP.put("func_71462_a", "func_71462_a");     // getConnection()
        METHOD_NAME_ONLY_MAP.put("func_71463_r", "func_71463_r");     // getIntegratedServer()
        METHOD_NAME_ONLY_MAP.put("func_71464_g", "func_71464_g");     // inGameHasFocus()
        METHOD_NAME_ONLY_MAP.put("func_71465_d", "func_71465_d");     // processKeyBinds()
        METHOD_NAME_ONLY_MAP.put("func_71466_n", "func_71466_n");     // updateKeyBindings()
        METHOD_NAME_ONLY_MAP.put("func_71467_a", "func_71467_a");     // addServerResourcePack(String)
        METHOD_NAME_ONLY_MAP.put("func_71469_m", "func_71469_m");     // getSnooper()
        METHOD_NAME_ONLY_MAP.put("func_71470_a", "func_71470_a");     // updateFogColor(float, ScaledResolution)
        METHOD_NAME_ONLY_MAP.put("func_71471_a", "func_71471_a");     // setSoundHandler(SoundHandler)
        METHOD_NAME_ONLY_MAP.put("func_71472_c", "func_71472_c");     // setDimensionAndSpawnPlayer(int)
        METHOD_NAME_ONLY_MAP.put("func_71473_m", "func_71473_m");     // clickBlock(BlockPos, EnumFacing)
        METHOD_NAME_ONLY_MAP.put("func_71474_y", "func_71474_y");     // setGameType(GameType)
        METHOD_NAME_ONLY_MAP.put("func_71475_a", "func_71475_a");     // renderStreamOverlay(String, int, int, int, int)
        METHOD_NAME_ONLY_MAP.put("func_71476_a", "func_71476_a");     // objectMouseOver(RayTraceResult)
        METHOD_NAME_ONLY_MAP.put("func_71477_z", "func_71477_z");     // addScheduledTask(Callable)
        METHOD_NAME_ONLY_MAP.put("func_71478_a", "func_71478_a");     // displayDebugInfo(DebugGUIProfiler)
        METHOD_NAME_ONLY_MAP.put("func_71479_Z", "func_71479_Z");     // freeTextTexture(FontRenderer)
        METHOD_NAME_ONLY_MAP.put("func_71480_h", "func_71480_h");     // shouldRenderClouds()
        METHOD_NAME_ONLY_MAP.put("func_71481_g", "func_71481_g");     // getLimitFramerate()
        METHOD_NAME_ONLY_MAP.put("func_71482_a", "func_71482_a");     // displayStackTooltip(List, ItemStack, int, int)
        METHOD_NAME_ONLY_MAP.put("func_71483_s", "func_71483_s");     // getLanguageManager()
        METHOD_NAME_ONLY_MAP.put("func_71484_a", "func_71484_a");     // loadWorld(WorldClient, String, IProgressUpdate, WorldSettings)
        METHOD_NAME_ONLY_MAP.put("func_71485_a", "func_71485_a");     // setSession(Session)
        METHOD_NAME_ONLY_MAP.put("func_71486_a", "func_71486_a");     // setRenderViewEntity(Entity)
        METHOD_NAME_ONLY_MAP.put("func_71487_g", "func_71487_g");     // isSameServer()
        METHOD_NAME_ONLY_MAP.put("func_71488_a", "func_71488_a");     // addScheduledTask(FutureTask)
        METHOD_NAME_ONLY_MAP.put("func_71489_b", "func_71489_b");     // init()
        METHOD_NAME_ONLY_MAP.put("func_71490_a", "func_71490_a");     // render(boolean, int)
        METHOD_NAME_ONLY_MAP.put("func_71491_a", "func_71491_a");     // getRenderItem()
        METHOD_NAME_ONLY_MAP.put("func_71492_a", "func_71492_a");     // getAmbientOcclusion()
        METHOD_NAME_ONLY_MAP.put("func_71493_ag", "func_71493_ag");   // isGamePaused()
        METHOD_NAME_ONLY_MAP.put("func_71494_a", "func_71494_a");     // addScheduledTask(Runnable)
        METHOD_NAME_ONLY_MAP.put("func_71495_a", "func_71495_a");     // getRenderManager()
        METHOD_NAME_ONLY_MAP.put("func_71496_a", "func_71496_a");     // getTextureManager()
        METHOD_NAME_ONLY_MAP.put("func_71497_a", "func_71497_a");     // getFile(String)
        METHOD_NAME_ONLY_MAP.put("func_71498_a", "func_71498_a");     // getCursorMetadata()
        METHOD_NAME_ONLY_MAP.put("func_71499_a", "func_71499_a");     // getShaderGroup()
        METHOD_NAME_ONLY_MAP.put("func_71500_a", "func_71500_a");     // resized(int, int)
        METHOD_NAME_ONLY_MAP.put("func_71501_a", "func_71501_a");     // updateWindowTitle()
        METHOD_NAME_ONLY_MAP.put("func_71502_a", "func_71502_a");     // getRenderViewEntity()
        METHOD_NAME_ONLY_MAP.put("func_71503_h", "func_71503_h");     // getCurrentServerData()
        METHOD_NAME_ONLY_MAP.put("func_71504_a", "func_71504_a");     // integratedServerHasMorePlayers()
        METHOD_NAME_ONLY_MAP.put("func_71505_a", "func_71505_a");     // getNetHandler()
        METHOD_NAME_ONLY_MAP.put("func_71506_a", "func_71506_a");     // getFontRenderer()
        METHOD_NAME_ONLY_MAP.put("func_71507_a", "func_71507_a");     // getResource(IResourceLocation)
        METHOD_NAME_ONLY_MAP.put("func_71508_a", "func_71508_a");     // getResourcePackRepository()
        METHOD_NAME_ONLY_MAP.put("func_71509_a", "func_71509_a");     // getAchievementMap()
        METHOD_NAME_ONLY_MAP.put("func_71510_a", "func_71510_a");     // getStatFileWriter()
        METHOD_NAME_ONLY_MAP.put("func_71511_b", "func_71511_b");     // setSoundLevel(float)
        METHOD_NAME_ONLY_MAP.put("func_71513_a", "func_71513_a");     // getRenderEngine()
        METHOD_NAME_ONLY_MAP.put("func_71514_a", "func_71514_a");     // getTextureMap()
        METHOD_NAME_ONLY_MAP.put("func_71515_a", "func_71515_a");     // getRenderList()
        METHOD_NAME_ONLY_MAP.put("func_71516_a", "func_71516_a");     // getFovModifier(float, boolean)
        METHOD_NAME_ONLY_MAP.put("func_71517_a", "func_71517_a");     // getRenderChunkCache()
        METHOD_NAME_ONLY_MAP.put("func_71518_a", "func_71518_a");     // getRenderDistance()
        METHOD_NAME_ONLY_MAP.put("func_71519_a", "func_71519_a");     // getChunkRenderDispatcher()
        METHOD_NAME_ONLY_MAP.put("func_71520_a", "func_71520_a");     // getRenderInfo()
        METHOD_NAME_ONLY_MAP.put("func_71521_a", "func_71521_a");     // getRenderGlobal()
        METHOD_NAME_ONLY_MAP.put("func_71522_a", "func_71522_a");     // getRenderEntity()
        METHOD_NAME_ONLY_MAP.put("func_71523_a", "func_71523_a");     // getRenderManager()
        METHOD_NAME_ONLY_MAP.put("func_71524_a", "func_71524_a");     // getRenderItem()
        METHOD_NAME_ONLY_MAP.put("func_71525_a", "func_71525_a");     // getRenderFont()
        METHOD_NAME_ONLY_MAP.put("func_71526_a", "func_71526_a");     // getRenderTexture()
        METHOD_NAME_ONLY_MAP.put("func_71527_a", "func_71527_a");     // getRenderBlock()

        // Item 系
        METHOD_NAME_ONLY_MAP.put("func_77656_e", "setMaxDamage");     // setMaxDamage(int)
        METHOD_NAME_ONLY_MAP.put("func_77627_a", "setHasSubtypes");   // setHasSubtypes(bool)
        METHOD_NAME_ONLY_MAP.put("func_77658_a", "getUnlocalizedName"); // getUnlocalizedName

        // Property 静的ファクトリ（descriptor 不一致時のフォールバック）
        // METHOD_MAP に descriptor 付きで登録済み。こちらは descriptor が
        // ジェネリクス消去などで変動した場合のセーフネット。
        METHOD_NAME_ONLY_MAP.put("func_177719_a", "create");  // PropertyInteger → IntegerProperty.create
        METHOD_NAME_ONLY_MAP.put("func_177716_a", "create");  // PropertyBool   → BooleanProperty.create
        METHOD_NAME_ONLY_MAP.put("func_177709_a", "create");  // PropertyEnum   → EnumProperty.create
    }

    @Override
    public Object mapValue(Object value) {
        if (value instanceof String string) {
            return map(string);
        }
        return super.mapValue(value);
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        // ── 既存の Entity final メソッド衝突回避 ────────────────────────
        if ("getX".equals(name) && "()D".equals(descriptor)) return "legacyGetX";
        if ("getY".equals(name) && "()D".equals(descriptor)) return "legacyGetY";
        if ("getZ".equals(name) && "()D".equals(descriptor)) return "legacyGetZ";
        if ("setPosition".equals(name) && "(DDD)V".equals(descriptor)) return "legacySetPosition";
        if ("setPositionAndRotation".equals(name) && "(DDDFF)V".equals(descriptor)) return "legacySetPositionAndRotation";

        // ── METHOD_MAP: owner + name + descriptor で完全一致 ────────────
        String mapped = METHOD_MAP.get(methodKey(owner, name, descriptor));
        if (mapped != null) return mapped;

        // ── METHOD_NAME_ONLY_MAP: 難読化名だけで一致（フォールバック）────
        //    func_XXXXXX_x 形式に限定してフォールバックを適用する
        if (name.startsWith("func_") && name.length() > 8) {
            String nameOnly = METHOD_NAME_ONLY_MAP.get(name);
            if (nameOnly != null) return nameOnly;
        }

        return super.mapMethodName(owner, name, descriptor);
    }

    @Override
    public String map(String internalName) {
        if (internalName == null) return null;

        String mapped = TYPE_MAP.get(internalName);
        if (mapped != null) return mapped;

        for (Map.Entry<String, String> entry : TYPE_MAP.entrySet()) {
            String from = entry.getKey();
            String to   = entry.getValue();
            if (internalName.startsWith(from + "/")) {
                return to + internalName.substring(from.length());
            }
        }

        return internalName;
    }
}
