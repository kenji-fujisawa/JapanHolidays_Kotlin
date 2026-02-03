#!/bin/bash

cd "$(dirname "$0")"

filename='../JapanHolidays/src/main/java/jp/uhimania/japanholidays/syukujitsu.kt'

curl -o $filename -L https://www8.cao.go.jp/chosei/shukujitsu/syukujitsu.csv

iconv -f Shift_JIS -t UTF-8 $filename | tr -d '\r' > $filename.tmp
mv $filename.tmp $filename

sed -i '.bak' '1d' $filename

sed -i '.bak' 's/^/"/' $filename
sed -i '.bak' 's/,/" to "/' $filename
sed -i '.bak' 's/$/",/' $filename

sed -i '.bak' '1i\
package jp.uhimania.japanholidays\
\
import java.util.concurrent.ConcurrentHashMap\
\
internal val holidays = ConcurrentHashMap(mapOf(
' $filename
sed -i '.bak' '$a\
))
' $filename

rm $filename.bak
