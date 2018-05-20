# Relatório
Uma análise aos resultados e ao impacto que a política otimista implementada pela FénixFramework tem na invocação dos serviços remotos.

## Análise dos resultados

### 100% Reads
| Label                | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min | Max   | Error %  | Throughput | Received KB/sec | Sent KB/sec |
|----------------------|-----------|---------|--------|----------|----------|----------|-----|-------|----------|------------|-----------------|-------------|
| Read RentACars       | 2500      | 3360    | 2177   | 7599     | 10349    | 13613    | 4   | 14737 | "0,000%" | "6,22281"  | "11,49"         | "0,77"      |
| Read Vehicle Data    | 2500      | 2634    | 1207   | 6854     | 9198     | 11047    | 3   | 14412 | "0,000%" | "6,22265"  | "14,77"         | "0,95"      |
| Read Renting Data    | 2500      | 515     | 26     | 1653     | 3936     | 4714     | 8   | 5711  | "0,000%" | "6,21696"  | "160,83"        | "1,11"      |
| Read Brokers         | 2500      | 3450    | 3023   | 7420     | 7988     | 10304    | 9   | 10977 | "0,000%" | "6,21739"  | "118,53"        | "0,76"      |
| Read Clients         | 5000      | 2996    | 1623   | 7781     | 9477     | 12050    | 2   | 14886 | "0,000%" | "12,43834" | "206,04"        | "1,66"      |
| Read Adventures      | 5000      | 1849    | 650    | 5927     | 6640     | 8247     | 2   | 10474 | "0,000%" | "12,44103" | "31,45"         | "1,93"      |
| Read Banks           | 2500      | 4388    | 3598   | 9310     | 11809    | 14138    | 4   | 15274 | "0,000%" | "6,23549"  | "8,32"          | "0,75"      |
| Read Accounts        | 2500      | 4862    | 3553   | 12707    | 14440    | 15118    | 13  | 15726 | "0,000%" | "6,27430"  | "192,68"        | "0,90"      |
| Read Hotels          | 2500      | 6470    | 6105   | 15201    | 17105    | 18239    | 5   | 21599 | "0,000%" | "6,27885"  | "12,04"         | "0,76"      |
| Read Rooms           | 2500      | 5483    | 4376   | 11875    | 14753    | 18717    | 8   | 22001 | "0,000%" | "6,28171"  | "88,56"         | "0,85"      |
| Read Providers       | 2500      | 2675    | 1747   | 7580     | 8675     | 9518     | 4   | 12753 | "0,000%" | "6,29390"  | "10,88"         | "0,78"      |
| Read Activities      | 2500      | 2907    | 1671   | 7263     | 9706     | 12897    | 2   | 15067 | "0,000%" | "6,32236"  | "13,04"         | "0,90"      |
| Read Offers          | 2500      | 2866    | 1780   | 7732     | 8557     | 12839    | 2   | 15094 | "0,000%" | "6,32908"  | "10,19"         | "0,99"      |
| Read TaxPayers       | 2500      | 4745    | 3751   | 10388    | 13889    | 23285    | 6   | 32065 | "0,000%" | "6,33105"  | "19,93"         | "0,79"      |
| Read ItemTypes       | 2500      | 5348    | 3032   | 14188    | 17898    | 22375    | 2   | 32069 | "0,000%" | "6,33169"  | "9,83"          | "0,79"      |
| Read Buyer Invoices  | 2500      | 6263    | 4530   | 16300    | 20129    | 25215    | 14  | 32035 | "0,000%" | "6,33197"  | "200,40"        | "0,91"      |
| Read Seller Invoices | 2500      | 7299    | 5809   | 18716    | 20491    | 23099    | 14  | 25943 | "0,000%" | "6,33726"  | "200,50"        | "0,91"      |
| TOTAL                | 48521     | 3763    | 2099   | 9594     | 13386    | 19286    | 2   | 57753 | "0,000%" | "81,23688" | "892,07"        | "11,70"     |

### 100% Writes
| Label             | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min | Max   | Error %  | Throughput | Received KB/sec | Sent KB/sec |
|-------------------|-----------|---------|--------|----------|----------|----------|-----|-------|----------|------------|-----------------|-------------|
| Process Adventure | 500       | 2452    | 1025   | 8115     | 8867     | 9782     | 5   | 10810 | "0,000%" | "37,38038" | "102,03"        | "14,64"     |

### 30% Writes 70% Reads
| Label             | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min | Max   | Error %   | Throughput  | Received KB/sec | Sent KB/sec |
|-------------------|-----------|---------|--------|----------|----------|----------|-----|-------|-----------|-------------|-----------------|-------------|
| Process Adventure | 6000      | 2057    | 1272   | 5136     | 7193     | 9113     | 0   | 10280 | "33,333%" | "71,99424"  | "156,25"        | "18,80"     |
| Read Adventures   | 2000      | 1240    | 1247   | 2235     | 2428     | 4247     | 2   | 5152  | "0,000%"  | "24,83392"  | "62,62"         | "3,86"      |
| Read Banks        | 2000      | 2180    | 1048   | 6524     | 7095     | 7400     | 2   | 7602  | "0,000%"  | "26,27672"  | "35,08"         | "3,16"      |
| Read Clients      | 2000      | 1733    | 1137   | 4873     | 6130     | 6759     | 6   | 8315  | "0,000%"  | "26,28086"  | "429,63"        | "3,49"      |
| Read Accounts     | 2000      | 1352    | 779    | 3622     | 4252     | 6593     | 7   | 7831  | "0,000%"  | "26,29987"  | "424,55"        | "3,78"      |
| Read Rooms        | 2000      | 4082    | 1648   | 14175    | 14538    | 17992    | 8   | 18239 | "2,900%"  | "27,32726"  | "375,75"        | "3,58"      |
| Read Activities   | 2000      | 1278    | 954    | 3036     | 3961     | 4297     | 3   | 4436  | "0,000%"  | "28,98341"  | "54,68"         | "4,10"      |
| Read Offers       | 2000      | 795     | 468    | 2130     | 2493     | 2796     | 2   | 3655  | "0,000%"  | "29,02210"  | "46,74"         | "4,53"      |
| TOTAL             | 21120     | 1788    | 1003   | 4384     | 6793     | 14132    | 0   | 18239 | "9,744%"  | "110,57650" | "674,96"        | "20,82"     |

## Análise
Como podemos observar, a política otimista implementada por parte da Fénix Framework assume que diversas transações vão ser concluídas sem interferirem entre si.
Esta política atrasa a verificação de integridade até ao final da transação, assim, possíveis conflitos apenas são detetados apenas após as alterações serem feitas (nesse caso de ocorrendo um rollback).
Pela análise dos resultados obtidos podemos verificar que o número de erros, para o teste 100Reads é inferior ao verificado no 30Writes consequência da probabilidade de conflito ser maior em sequências de  writes e reads alternados.
O erro dos 100Writes continua a 0, mas pensamos que esta facto deve-se a termos um numero reduzido de threads neste teste. O throughput total continua a ser menor que ambos os outros testes.
Em parte, os erros devem-se ao facto do método 'process' utilizar diversas bases de dados, sendo por isso necessária sincronização, e diminuindo a escalabilidade da aplicação.
