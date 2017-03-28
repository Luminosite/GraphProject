#!/bin/bash

#if [[ $# -lt 2 ]]
#then
#  echo "Usage:"
#  echo "    $0 today_date folder_path"
#
#else

  ts=`date +%Y%m%d_%H%M%S`
  time ./doSubmit.sh sparkx.trial.GraphTrial 2>&1 | tee graph_trial_${ts}_log
#fi