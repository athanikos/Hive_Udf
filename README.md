# Udfs
A custom UDF that uses an hdfs file as a lookup file where the left part is the name of the formula and the right part is the formula.
Similar to :
https://www.inovex.de/blog/hive-udf-lookups/
With the right part of the formula being any expression supported by janino 
Tested with HDP 2.6 

# use 
DROP FUNCTION ComputeField;
CREATE FUNCTION ComputeField as 'crmlbd.udfs.ComputeField'  
USING JAR 'hdfs:///<somepath>/udfs-0.0.300-SNAPSHOT.jar';

select ComputeField('FormualName',1,'hdfs://<namenode>:8020/user/admin/act');

select ComputeField('FormualName', A.id,'hdfs://<namenode>:8020/user/admin/lookups/lookup')
from 
(
  select max(merge_source.id) id
  from merge_source
  group by merge_source.id 
)  as A


Example lookup entry: 
FormualName|x==0 ? "0A" : x==1 ? "01" :  x==2 ? "02" : x==3 ? "03" : x==4 ? "04" :  x==5 ? "05" :  x==6 ? "06" : x==7 ? "07" : x==8 ? "08" : x==9 ? "09" : "00"
