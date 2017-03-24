#!/bin/bash

if [[ $# -lt 2 ]]
then
  echo "Usage:"
  echo "    $0 today_date folder_path"

else

  dateToday=$1
  datePit=2015/08/15
  folder=$2
  time ./doSubmit.sh validation.AccountValidation -t ${dateToday} -p ${datePit} -D -f ${folder} \
    2>&1 | tee ${dateToday//'/'/''}_log
fi