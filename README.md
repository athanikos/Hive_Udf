# Udfs
A custom UDF that uses an hdfs file as a lookup file where the left part is the name of the formula and the right part is the formula.


# use 
DROP FUNCTION ComputeField;
CREATE FUNCTION ComputeField as 'crmlbd.udfs.ComputeField'  
USING JAR 'hdfs:///user/admin/udfs-0.0.300-SNAPSHOT.jar';

select ComputeField('ActiveCards',1,'hdfs://<namenode>:8020/user/admin/act');

select ComputeField('ActiveCards', A.id,'hdfs://<namenode>:8020/user/admin/lookups/lookup')
from 
(
  select max(merge_source.id) id
  from merge_source
  group by merge_source.id 
)  as A
