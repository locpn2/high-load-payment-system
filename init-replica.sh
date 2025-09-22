#!/bin/bash

# Script to initialize PostgreSQL replica
# This script ensures the replica container stays running after initialization

set -e

echo "Starting PostgreSQL replica initialization..."

# Set proper ownership and permissions for data directory
chown -R postgres:postgres /var/lib/postgresql/data
chmod 700 /var/lib/postgresql/data

# Wait for primary database to be ready
echo "Waiting for primary database to be ready..."
until pg_isready -h postgres -p 5432 -U example; do
  echo "Waiting for postgres..."
  sleep 2
done

# Clean existing data directory
echo "Cleaning existing data directory..."
rm -rf /var/lib/postgresql/data/*

# Perform base backup from primary
echo "Performing base backup from primary database..."
pg_basebackup -h postgres -p 5432 -U example -D /var/lib/postgresql/data -F p -Xs -P -R

# Set proper ownership for the backup data
chown -R postgres:postgres /var/lib/postgresql/data

# Start PostgreSQL in replica mode using exec to replace the shell process
echo "Starting PostgreSQL replica server..."
exec postgres -c 'hot_standby=on' -c 'listen_addresses=*'
