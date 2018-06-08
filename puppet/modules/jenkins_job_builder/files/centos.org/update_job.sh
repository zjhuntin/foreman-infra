#!/bin/bash

jenkins-jobs --conf ./jenkins_job.ini update -r jobs/ $1
