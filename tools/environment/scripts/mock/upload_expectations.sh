#!/bin/bash

MOCKSERVER_URL=${1:-http://localhost:1080}
DIR=${2:-tools/environment/mock/expectations}

find "$DIR" -type f -name "*.json" | while read -r file; do
  printf "\n⏳ Uploading '%s'\n" "$file"
  if [[ "${CI:-}" == "true" ]]; then
    container_id="$(docker compose -f tools/environment/docker/docker-compose.yml \
      -f tools/environment/docker/docker-compose.ci.yml \
      ps -q mock-server | head -n1 || true)"
    if [[ -z "$container_id" ]]; then
      echo "❌ MockServer container not found"
      exit 1
    fi
    cat "$file" | docker run --rm -i --network "container:${container_id}" curlimages/curl:8.5.0 \
      -s -X PUT "http://localhost:1080/mockserver/expectation" \
      -d @- \
      -H "Content-Type: application/json"
  else
    curl -s -X PUT "$MOCKSERVER_URL/mockserver/expectation" \
      -d @"$file" \
      -H "Content-Type: application/json"
  fi
done

printf "\n✅ All expectations uploaded.\n"
