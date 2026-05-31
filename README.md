# 料理群峦
## 这是一个将群峦营养值（谷物、水果、蔬菜、肉类、乳制品）动态地加入到料理乐事中的模组。

### 对原模组的更改 & 本模组的特性
1. 菜品数量为3。
2. 饱和度大小取决于食物总数量。
3. 每种营养值默认的最大值为6。
4. 营养值与品质（分数）相关。
5. 成品食物最多只存在4种营养（若有5种则会移除最少的那个）。

### 补充（KubeJS兼容）
添加了KubeJS的Binding用于方便的写JSON。（当然，你也可以选择直接写数据包，以下就是使用KubeJS写数据的例子）

在本模组中添加了一个新数据，用于解决烹饪物品的营养值情况（比如，蛋是没有营养值的，因此可以通过读取这份烹饪营养值来提供成品菜的营养值）
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson(
        // data文件的路径 `${命名空间}:cuisinetfc/cooked_food/${文件名字}`
        'kubejs:cuisinetfc/cooked_food/example.json',
        
        // 填写你想烹饪后食物提供的营养，否则烹饪使用的是默认食物提供的营养
        CookedFoodData.create("tfc:food/sugarcane", c => {
            c.vegetables(1.5)
        })
    )

    event.addJson(
        'kubejs:cuisinetfc/cooked_food/example_1.json',
        CookedFoodData.create("firmalife:raw_honey", c => {
            c.dairy(1)
        })
    )
})
```
上述代码成立的条件是该食物具有烹饪数据
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson(
        // data文件的路径 `${命名空间}:cuisinedelight_config/ingredient/${文件名字}`
        'kubejs:cuisinedelight_config/ingredient/examples.json',
        
        // 创建食物烹饪数据的json
        CuisineConfigData.create(
            CuisineConfigData.data("tfc:food/sugarcane", c => {
                c.type("veg")
                .minTime(20)
                .maxTime(200)
                .stirTime(40)
                .size(1)
                .effects(e => {
                    e.add("minecraft:speed", 1, 200)
                })
            }),

            CuisineConfigData.data("firmalife:raw_honey", c => {
                c.size(0)
            })
        )
    )
})
```
最后还有物品在炒锅展示，是可选的，可写可不写
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson(
        'kubejs:cuisinedelight_config/transform/example_0.json',
        CuisineTransformData.create(c => {
            c.itemTransform("tfc:food/sugarcane", "minecraft:sugar", "cooked")
            c.fluidTransform("firmalife:raw_honey", -77010)
        })
    )
})
```

---
# Cuisine TFC
## This is a mod for adding dynamic TerraFirmaCraft nutrients(grain, fruit, vegetable, protein, dairy) to Cuisine Delight.

### Changes to the original mod & The traits of mod
1. It will set the food size to 3.
2. TFC saturation value is related to total food item count.
3. The default maximum value for each nutrient is 6.
4. Nutrient value is related to the score of the food.
5. Finished food can only contain up to 4 nutrients(If it contains 5 nutrients, the least one will be removed).

### KubeJS Compat
Added KubeJS Binding for convenient JSON writing. (Of course, you can also choose to write the datapack directly. Below is an example of writing data using KubeJS)

This mod add a new data for solve item nutrients. (Such as, egg has no nutrients, so the nutrients of the finished food can be provided by reading the cooked food data)
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson(
        // data file path `${namespace}:cuisinetfc/cooked_food/${file_name}`
        'kubejs:cuisinetfc/cooked_food/example.json',

        // The nutrients provided by food after cooking, otherwise the default nutrients of the food is used.
        CookedFoodData.create("tfc:food/sugarcane", c => {
            c.vegetables(1.5)
        })
    )

    event.addJson(
        'kubejs:cuisinetfc/cooked_food/example_1.json',
        CookedFoodData.create("firmalife:raw_honey", c => {
            c.dairy(1)
        })
    )
})
```
The above code's condition is that the food has cuisine config.
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson(
        // data file path `${namespace}:cuisinedelight_config/ingredient/${file_name}`
        'kubejs:cuisinedelight_config/ingredient/examples.json',
        CuisineConfigData.create(
            CuisineConfigData.data("tfc:food/sugarcane", c => {
                c.type("veg")
                    .minTime(20)
                    .maxTime(200)
                    .stirTime(40)
                    .size(1)
                    .effects(e => {
                        e.add("minecraft:speed", 1, 200)
                    })
            }),

            CuisineConfigData.data("firmalife:raw_honey", c => {
                c.size(0)
            })
        )
    )
})
```
Lastly, item display in cuisine skillet, optional.
```JavaScript
ServerEvents.highPriorityData(event => {
    event.addJson(
        'kubejs:cuisinedelight_config/transform/example_0.json',
        CuisineTransformData.create(c => {
            c.itemTransform("tfc:food/sugarcane", "minecraft:sugar", "cooked")
            c.fluidTransform("firmalife:raw_honey", -77010)
        })
    )
})
```