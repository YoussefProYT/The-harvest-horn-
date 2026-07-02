# Harvest Horn Mod - دليل الإعداد والشرح

## المتطلبات
- **Fabric Loom** (Gradle plugin) - نفس الإعداد اللي استخدمته قبل كده للمشاريع التانية
- **Fabric API** - الآيتم بيستخدم `Fertilizable` interface اللي موجود في vanilla، مش محتاج API إضافي كبير، بس Fabric API مهم للـ ItemGroupEvents

## خطوات التركيب في مشروعك

### 1. لو مش عندك مشروع Fabric جاهز:
استخدم [Fabric Example Mod](https://github.com/FabricMC/fabric-example-mod) كنقطة بداية، أو الـ template بتاعك من مشروع الـ combat mod اللي بتشتغل عليه.

### 2. حط الملفات في المكان الصح:
```
src/main/java/com/yousef/harvesthorn/
├── HarvestHorn.java
└── item/
    ├── HarvestHornItem.java     (كلاس عام لكل التيرز)
    └── ModItems.java            (تسجيل الأربع تيرز)

src/main/resources/
├── assets/harvesthorn/
│   ├── lang/en_us.json
│   └── models/item/
│       ├── iron_harvest_horn.json
│       ├── gold_harvest_horn.json
│       ├── diamond_harvest_horn.json
│       └── netherite_harvest_horn.json
└── data/harvesthorn/recipe/
    ├── iron_harvest_horn.json
    ├── gold_harvest_horn.json
    ├── diamond_harvest_horn.json
    └── netherite_harvest_horn.json
```

### 3. ضبط `fabric.mod.json`:
```json
{
  "schemaVersion": 1,
  "id": "harvesthorn",
  "version": "1.0.0",
  "name": "Harvest Horn",
  "entrypoints": {
    "main": ["com.yousef.harvesthorn.HarvestHorn"]
  },
  "depends": {
    "fabricloader": ">=0.15.0",
    "fabric-api": "*",
    "minecraft": "~1.21.1"
  }
}
```

### 4. Texture - كل التيرز بتستخدم texture الـ Goat Horn الأصلي:
كل تير له item id مختلف، فمحتاج model file منفصل بيه (بس كلهم بيشاوروا على نفس الـ texture):

```
src/main/resources/assets/harvesthorn/models/item/iron_harvest_horn.json
src/main/resources/assets/harvesthorn/models/item/gold_harvest_horn.json
src/main/resources/assets/harvesthorn/models/item/diamond_harvest_horn.json
src/main/resources/assets/harvesthorn/models/item/netherite_harvest_horn.json
```
كل واحد فيهم بنفس المحتوى:
```json
{
  "parent": "item/handheld",
  "textures": {
    "layer0": "minecraft:item/goat_horn"
  }
}
```
لو عايز تفرّق بينهم بصريًا بعدين (مثلًا لون مختلف لكل تير)، هتحتاج تعمل PNG منفصل لكل واحد بدل ما كلهم يشاوروا على نفس texture الـ vanilla.

### 5. Crafting Recipes:
عملتلك 4 ملفات ريسيبي - كل واحد يروح في:
```
src/main/resources/data/harvesthorn/recipe/iron_harvest_horn.json
src/main/resources/data/harvesthorn/recipe/gold_harvest_horn.json
src/main/resources/data/harvesthorn/recipe/diamond_harvest_horn.json
src/main/resources/data/harvesthorn/recipe/netherite_harvest_horn.json
```
كلهم Shaped 3x3 (8 مادة حوالين Goat Horn في النص) زي الصور اللي بعتها بالظبط.

### 6. Lang file:
```
src/main/resources/assets/harvesthorn/lang/en_us.json
```
```json
{
  "item.harvesthorn.iron_harvest_horn": "Iron Harvest Horn",
  "item.harvesthorn.gold_harvest_horn": "Gold Harvest Horn",
  "item.harvesthorn.diamond_harvest_horn": "Diamond Harvest Horn",
  "item.harvesthorn.netherite_harvest_horn": "Netherite Harvest Horn"
}
```

## إزاي اللوجيك شغال؟ (النسخة المطوّرة)

### الميكانيزم الأساسي عند النفخة
لكل بلوك في مساحة التير (5x5 / 10x10 / 15x15 / 20x20):

1. **لو المحصول ناضج (mature)** → بيتحصد فورًا:
   - كل الـ drops بتتاخد باستخدام `Block.getDroppedStacks` (نفس الطريقة اللي اللعبة بتستخدمها لما تكسر البلوك بإيدك أو بمعول)
   - لو المحصول محتاج seed لإعادة الزراعة (قمح، بنجر)، بنحجز وحدة واحدة من الـ drops قبل ما تدخل الإنفنتوري
   - البلوك بيترجع لـ `age 0` تلقائيًا = إعادة زراعة فورية
   - **Nether Wart استثناء:** مش بياخد/يحجز seed لأنه بيترزرع من غير استهلاك آيتم زي القمح بالظبط

2. **لو المحصول لسه بينمو** → بتاخد مرحلة نمو واحدة (زي الـ Bonemeal بالظبط، عن طريق `CropBlock.grow()`)

3. **بلوكات تانية Fertilizable** (زي stems البطيخ والقرع) → نمو بس من غير حصاد (الفاكهة نفسها بلوك منفصل، مش جزء من الـ AGE property)

### إدارة الإنفنتوري
- كل drop بيتحاول يدخل إنفنتوري اللاعب أولًا (`player.getInventory().insertStack()`)
- لو الإنفنتوري مليان والـ stack فضل فيه حاجة معملتش دخول، بترمي كـ `ItemEntity` على الأرض عند مكان المحصول بالظبط

### الفرق بين التيرز

| التير | المساحة | Max Uses | المكوّنات |
|---|---|---|---|
| Iron | 5x5 | 100 | 8x Iron Ingot + Goat Horn |
| Gold | 10x10 | 200 | 8x Gold Ingot + Goat Horn |
| Diamond | 15x15 | 350 | 8x Diamond + Goat Horn |
| Netherite | 20x20 | 500 | 8x Netherite Scrap + Goat Horn |

كل الريسيبيز **Shaped 3x3** (زي الصور اللي بعتها بالظبط) - المادة في الـ 8 خانات حوالين، وأي Goat Horn (بأي instrument) في النص.

✅ **الـ Netherite tier** بيستخدم `minecraft:netherite_scrap` (مش Ingot) عشان يبقى أرخص وأسهل تجميع - ده أنسب فعلًا لأن الـ Scrap لوحده بدون Gold Ingots ملوش استخدام مباشر تاني غير الـ Smithing، فده بيدّيله purpose إضافي.

### الصوت
البوق بيستخدم `SoundEvents.ITEM_GOAT_HORN_SOUND_0` وده أصلاً صوت الـ **Ponder Horn** - مفيش تغيير مطلوب هنا، كان مظبوط من الأول.

## أفكار لتطوير المود أكتر (لما يجهز الأساسي)

- **Config file** (باستخدام Fabric's Config API أو AutoConfig) عشان الـ RADIUS والـ COOLDOWN يبقوا قابلين للتعديل من غير ما تعمل recompile
- **Sound مختلف** بدل صوت الـ Goat Horn العادي - لازم custom sound event
- **XP cost** بدل الـ durability، يعني كل نفخة تاخد منك XP levels
- **Particle مختلفة** حسب نوع المحصول اللي نما (أخضر للقمح، أصفر للجزر...الخ)
- **دائرة حقيقية** بدل مربع، باستخدام حساب المسافة الإقليدية

## ملاحظة عن الإصدار

افترضت إنك قصدك **Minecraft 1.21.1** بناءً على مشروعك التاني (الـ combat mod) - لو فعلاً قصدك إصدار مختلف قولّي وهظبط الـ dependencies في `fabric.mod.json` والـ mappings.
