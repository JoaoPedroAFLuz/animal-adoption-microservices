#!/bin/bash
/opt/keycloak/bin/kc.sh start-dev --import-realm --spi-realm-default-ssl-required=NONE &
KC_PID=$!

# Wait for Keycloak to be ready
until /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080 --realm master --user "$KEYCLOAK_ADMIN" --password "$KEYCLOAK_ADMIN_PASSWORD" 2>/dev/null; do
    sleep 2
done

# Disable SSL on master realm
/opt/keycloak/bin/kcadm.sh update realms/master -s sslRequired=NONE

wait $KC_PID
