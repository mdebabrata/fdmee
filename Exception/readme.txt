Script to debug FDMEE exception:
EPMFDM-140274:Message - Exception while applying <column_name> mapping on the row

Example
2016-03-22 03:24:27,327 DEBUG [AIF]: EPMFDM-140274:Message - Exception while applying CURKEY mapping on the row:["FY14","Plan","Target","840","P_000","NI",-99153.82000000001]

Instructions:
1. Extract header info from log file. It will be in syntax. HEADER: *
2. Replace String[] header with header info.
3. Extract data row from exception message and replace in String row. Reformat row as "\"FY14\",\"Plan\",\"Working\",\"000\",\"P_000\",\"DSO\",45".
4. Execute debug script to check the case of exception.

Seems like exceptions are generated from last if block. Change dataSyncFlag to match with exception in log file.
Possible Exception Scenario:
a) Amount Column
b) Column does not exist in header
