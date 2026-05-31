# 料理群峦
## 这是一个将群峦营养值（谷物、水果、蔬菜、肉类、乳制品）动态地加入到料理乐事中的模组。

### 对原模组的更改 & 本模组的特性
1. 菜品数量为3。
2. 饱和度大小取决于食物总数量。
3. 每种营养值默认的最大值为6。
4. 营养值与品质（分数）相关。
5. 成品食物最多只存在4种营养（若有5种则会移除最少的那个）。

### 补充（KubeJS兼容）
在本模组中添加了一个新数据，用于解决烹饪物品的营养值情况（比如，蛋是没有营养值的，因此可以通过读取这份烹饪营养值来提供成品菜的营养值）
```JavaScript
CuisineTFCEvent.data(event => {
    event.cookedFood("minecraft:honey_bottle", [0, 0, 0, 0, 1])
    event.cookedFood("tfc:food/sugarcane", [0, 0, 1, 0, 0])

    // event.cookedFoodJson({
    //     ingredient: {
    //         item: "tfc:food/sugarcane"
    //     },
    //     nutrients: [0, 0, 1, 0, 0]
    // })
})
```
上述代码成立的条件是该食物具有烹饪数据
```JavaScript
CuisineTFCEvent.data(event => {
    event.ingredientConfig(
        "tfc:food/sugarcane",
        c => {
            c.type("veg")
                .minTime(40)
                .maxTime(200)
                .effects(e => {
                    e.add("minecraft:speed", 1, 200)
                    e.add("minecraft:absorption", 0, 400)
                })
        }
    )

    // event.ingredientConfigJson({
    //     entries:[
    //         {
    //             ingredient:{
    //                 item: "tfc:food/sugarcane"
    //             },
    //             type:"veg",
    //             min_time:40,
    //             max_time:200,
    //             stir_time:60,
    //             raw_penalty:0.5,
    //             overcook_penalty:0.5,
    //             size:1,
    //             nutrition:1,
    //             effects:[
    //                 {
    //                     effect:"minecraft:speed",
    //                     level:1,
    //                     time:200
    //                 },
    //                 {
    //                     effect:"minecraft:absorption",
    //                     level:0,
    //                     time:400
    //                 }
    //             ]
    //         }
    //     ]
    // })
})
```
最后还有物品在炒锅展示，是可选的，可写可不写
```JavaScript
CuisineTFCEvent.data(event => {
    event.itemTransform("tfc:food/sugarcane", "minecraft:sugar", "cooked")
    event.fluidTransform("firmalife:food/raw_honey", -77010)

    // event.transformJson({
    //     itemTransform:{
    //         "tfc:food/sugarcane":{
    //             next:"minecraft:sugar",
    //             stage:"cooked"
    //         }
    //     },
    //     fluidTransform:{
    //         "firmalife:food/raw_honey":{
    //             color: -77010
    //         }
    //     }
    // })
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
CuisineTFCEvent.data(event => {
    event.cookedFood("minecraft:honey_bottle", [0, 0, 0, 0, 1])
    event.cookedFood("tfc:food/sugarcane", [0, 0, 1, 0, 0])

    // event.cookedFoodJson({
    //     ingredient: {
    //         item: "tfc:food/sugarcane"
    //     },
    //     nutrients: [0, 0, 1, 0, 0]
    // })
})
```
The above code's condition is that the food has cuisine config.
```JavaScript
CuisineTFCEvent.data(event => {
    event.ingredientConfig(
        "tfc:food/sugarcane",
        c => {
            c.type("veg")
                .minTime(40)
                .maxTime(200)
                .effects(e => {
                    e.add("minecraft:speed", 1, 200)
                    e.add("minecraft:absorption", 0, 400)
                })
        }
    )

    // event.ingredientConfigJson({
    //     entries:[
    //         {
    //             ingredient:{
    //                 item: "tfc:food/sugarcane"
    //             },
    //             type:"veg",
    //             min_time:40,
    //             max_time:200,
    //             stir_time:60,
    //             raw_penalty:0.5,
    //             overcook_penalty:0.5,
    //             size:1,
    //             nutrition:1,
    //             effects:[
    //                 {
    //                     effect:"minecraft:speed",
    //                     level:1,
    //                     time:200
    //                 },
    //                 {
    //                     effect:"minecraft:absorption",
    //                     level:0,
    //                     time:400
    //                 }
    //             ]
    //         }
    //     ]
    // })
})
```
Lastly, item display in cuisine skillet, optional.
```JavaScript
CuisineTFCEvent.data(event => {
    event.itemTransform("tfc:food/sugarcane", "minecraft:sugar", "cooked")
    event.fluidTransform("firmalife:food/raw_honey", -77010)

    // event.transformJson({
    //     itemTransform:{
    //         "tfc:food/sugarcane":{
    //             next:"minecraft:sugar",
    //             stage:"cooked"
    //         }
    //     },
    //     fluidTransform:{
    //         "firmalife:food/raw_honey":{
    //             color: -77010
    //         }
    //     }
    // })
})
```