## Introduction 
A custom UDF for Hive that uses an hdfs file as a lookup file. The implementation is similar  [to](https://www.inovex.de/blog/hive-udf-lookups/) with the addition of [Janino](https://janino-compiler.github.io/janino/) compiler as an expresison evaluator.
This allows to evaluate expressions rather than writting "case when" statements or even create complex formulas.
In addition, performance wise the hdfs lookup file  is emmited locally to all mappers, so it is fast.
Tested with HDP 2.6 (works with TEZ and MR ) 

## use 

```
DROP FUNCTION ComputeField;
CREATE FUNCTION ComputeField as 'crmlbd.udfs.ComputeField'  
USING JAR 'hdfs:///<somepath>/udfs-0.0.300-SNAPSHOT.jar';
// this will pass A.id as a parameter and will match key "FormulaName" in a lookupfile 
select ComputeField('FormulaName', A.id,'hdfs://<namenode>:8020/user/admin/lookups/lookupfile')
from 
(
  select max(merge_source.id) id
  from merge_source
  group by merge_source.id 
)  as A
```

## Example of a lookup entry in lookupfile: 
FormulaName|x==0 ? "0A" : x==1 ? "01" :  x==2 ? "02" : x==3 ? "03" : x==4 ? "04" :  x==5 ? "05" :  x==6 ? "06" : x==7 ? "07" : x==8 ? "08" : x==9 ? "09" : "00"
