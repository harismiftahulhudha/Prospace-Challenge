#!/bin/sh
adb shell run-as co.harismiftahulhudha.prospacetest chmod 777 /data/data/co.harismiftahulhudha.prospacetest/databases/
adb shell run-as co.harismiftahulhudha.prospacetest chmod 777 /data/data/co.harismiftahulhudha.prospacetest/databases/image_machine.db
adb shell run-as co.harismiftahulhudha.prospacetest cp /data/data/co.harismiftahulhudha.prospacetest/databases/image_machine.db /sdcard/
adb pull /sdcard/image_machine.db
