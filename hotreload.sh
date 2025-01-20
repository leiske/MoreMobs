for classfile in $(find build/mod/ -name "*.class"); do
    classname=$(echo $classfile | sed 's|build/mod/||; s|/|.|g; s|.class$||')
    echo "redefine $classname $classfile" | jdb -attach $1:5005 > /dev/null 2>&1
done
