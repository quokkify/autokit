#!/bin/bash
docker compose \
  -f tools/environment/docker/docker-compose.yml \
   down -v
