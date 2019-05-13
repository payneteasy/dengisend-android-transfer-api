#!/bin/bash

set -eux

swagger-codegen generate -i swagger.yml -l java -c swagger-codegen-config.json
