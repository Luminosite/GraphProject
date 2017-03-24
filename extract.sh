cd target/pack/script/python/Classifiers_v2
for x in `ls *`
do
    echo "Extracting $x"
    gunzip -d $x
done
