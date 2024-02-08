#!/bin/bash

# Set your username and password
USERNAME="admin"
PASSWORD="admin"

# Define the URL of the authentication endpoint
AUTH_URL="http://localhost:8080/api/authenticate"

# Send a POST request to authenticate and retrieve the JWT token
TOKEN=$(curl -X POST -s -H "Content-Type: application/json" -d '{"username":"'"$USERNAME"'","password":"'"$PASSWORD"'"}' $AUTH_URL | jq -r '.id_token')

# Check if the token is empty (authentication failed)
if [ -z "$TOKEN" ]; then
  echo "Authentication failed. No token received."
fi

# Use the retrieved token to make authenticated requests to other endpoints
# Example: Send a GET request to a protected endpoint
curl -X GET -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/spotify/exchange-code