# JapanHolidays

日本の祝日判定と祝日名の取得を行なう Kotlin ライブラリ  
[内閣府の祝日データ](https://www8.cao.go.jp/chosei/shukujitsu/gaiyou.html) を使用して祝日を判定します

## 導入

```
repositories {
    maven { url = uri("https://raw.githubusercontent.com/kenji-fujisawa/JapanHolidays_Kotlin/master/repository") }
}

dependencies {
    implementation("jp.uhimania.japanholidays:japanholidays:1.0.1")
}
```

## 使用方法

### 祝日判定

```
import jp.uhimania.japanholidays.Holidays

Holidays.isHoliday(2026, 1, 12)                                 // true
Holidays.isHoliday(2025, 1, 12)                                 // false

val formatter = SimpleDateFormat("yyyy-MM-dd")
Holidays.isHoliday(formatter.parse("2026-01-12") ?: Date())     // true
Holidays.isHoliday(formatter.parse("2025-01-12") ?: Date())     // false

Holidays.isHoliday(LocalDate.of(2026, 1, 12))                   // true
Holidays.isHoliday(LocalDate.of(2025, 1, 12))                   // false
```

### 祝日名の取得

```
import jp.uhimania.japanholidays.Holidays

Holidays.getName(2026, 1, 12)                               // "成人の日"
Holidays.getName(2025, 1, 12)                               // null

val formatter = SimpleDateFormat("yyyy-MM-dd")
Holidays.getName(formatter.parse("2026-01-12") ?: Date())   // "成人の日"
Holidays.getName(formatter.parse("2025-01-12") ?: Date())   // null

Holidays.getName(LocalDate.of(2026, 1, 12))                 // "成人の日"
Holidays.getName(LocalDate.of(2025, 1, 12))                 // null
```

## 補足

初期状態で 2027 年までの祝日データを内包していますが、以降はアプリ起動時に 30 日おきに最新データをチェックし、SQLite にキャッシュします

また、アプリ起動時に非同期で SQLite を読み込むので、メソッドの呼び出し時点で初期化が完了していなかった場合は正しい結果が得られない可能性があります  
そのような場合は `joinInit()` で初期化完了を待機してください

```
    var text by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        Holidays.joinInit()
        text = Holidays.getName(2026, 1, 12) ?: ""
    }

    Text(
        text = text,
        modifier = modifier
    )
```

なんらかの理由で初期化を中断したい場合は `cancelInit()` を使用してください

```
    Holidays.cancelInit()
```

## Lisence

This project is licensed under the MIT License, see the LICENSE.txt file for details
