#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-SCRIPT
  CREATE USER dbuser WITH PASSWORD 'reWy6228eUP9Nlc3';
  CREATE DATABASE mediassistant_db OWNER dbuser;

  \c mediassistant_db;

  GRANT ALL ON SCHEMA public TO dbuser;

  CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SCRIPT