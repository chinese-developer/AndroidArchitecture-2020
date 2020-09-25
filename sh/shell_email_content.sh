# 根据不同平台用于动态替换 email_template.txt 文件内 version, apkName, platform 字段, 重新生成对应平台的 txt 文本, 用于发送邮件.
apkNameReplace=$1
versionNameReplace=$2
platformReplace=$3

sed -e 's/version/'"$versionNameReplace"'/g' -e 's/apkName/'"$apkNameReplace"'/g' -e 's/platform/'"$platformReplace"'/g' ./sh/email_template.txt > ./sh/"$platformReplace".txt