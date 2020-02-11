package http.authz

import input

# disallow access to all by default
default allow = false

# decode JWT token to read the payload
token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.headers.jwtToken)
}

# Allow authenticated users to invoke methods.
allow {
  input.method == "POST"
  input.path[0] == "api"
  input.auth.principal.assuranceLevel == 2 	# assuranceLevel check for valid users
  token.payload.iss == "Vets.gov" 			# Ensure the issuer claim is the expected value
}

# Allow authenticated users to invoke methods.
allow {
  input.method == "POST"
  input.path[0] == "api"
  input.auth.principal.assuranceLevel == 1 	# assuranceLevel check for valid users
  token.payload.iss == "Vets.gov" 			# Ensure the issuer claim is the expected value
}

# Allow authenticated users to invoke methods.
allow {
  input.method  == "GET"
  input.path[0] == "api"
  input.auth.principal.assuranceLevel == 1 	# assuranceLevel check for valid users
  token.payload.iss == "Vets.gov" 			# Ensure the issuer claim is the expected value
}

# Allow authenticated users to invoke methods.
allow {
  input.method  == "GET"
  input.path[0] == "api"
  input.auth.principal.assuranceLevel == 2 	# assuranceLevel check for valid users
  token.payload.iss == "Vets.gov" 			# Ensure the issuer claim is the expected value
}

# Allow admin users to invoke any methods.
admin {
  input.auth.principal.assuranceLevel == 3 	# fake assuranceLevel just to demonstrate
}