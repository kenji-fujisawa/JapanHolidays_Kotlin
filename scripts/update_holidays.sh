#!/bin/bash

cd "$(dirname "$0")"
cd ..

update_syukujitu_kt() {
    local filename='JapanHolidays/src/main/java/jp/uhimania/japanholidays/syukujitsu.kt'

    curl -o $filename -L https://www8.cao.go.jp/chosei/shukujitsu/syukujitsu.csv

    iconv -f Shift_JIS -t UTF-8 $filename | tr -d '\r' > $filename.tmp
    mv $filename.tmp $filename

    sed -i'.bak' '1d' $filename

    sed -i'.bak' 's/^/"/' $filename
    sed -i'.bak' 's/,/" to "/' $filename
    sed -i'.bak' 's/$/",/' $filename

    sed -i'.bak' '1i\
package jp.uhimania.japanholidays\
\
import java.util.concurrent.ConcurrentHashMap\
\
internal val holidays = ConcurrentHashMap(mapOf(
' $filename
    sed -i'.bak' '$a\
))
' $filename

    rm $filename.bak
}

update_readme() {
    local old_year=$1
    local new_year=$2
    local filename="README.md"

    sed -i'.bak' "s/初期状態で $old_year 年/初期状態で $new_year 年/" $filename

    rm $filename.bak
}

filename='JapanHolidays/src/main/java/jp/uhimania/japanholidays/syukujitsu.kt'

old_year=$(grep '" to "' $filename | tail -n 1 | awk '{print $1}' | sed 's/"//g' | cut -d '/' -f 1)

update_syukujitu_kt

new_year=$(grep '" to "' $filename | tail -n 1 | awk '{print $1}' | sed 's/"//g' | cut -d '/' -f 1)

update_readme $old_year $new_year
