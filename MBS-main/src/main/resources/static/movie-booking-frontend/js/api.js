const API_BASE_URL = 'http://localhost:8080/api';

// Helper function to get JWT token from localStorage
function getAuthToken() {
  return localStorage.getItem('jwtToken');
}

// Helper function to get headers with Authorization if token exists
function getHeaders(isJson = true) {
  const headers = {};
  if (isJson) {
    headers['Content-Type'] = 'application/json';
  }
  const token = getAuthToken();
  if (token) {
    headers['Authorization'] = 'Bearer ' + token;
  }
  return headers;
}

// Generic GET request
async function apiGet(endpoint) {
  const response = await fetch(API_BASE_URL + endpoint, {
    method: 'GET',
    headers: getHeaders(),
  });
  if (!response.ok) {
    throw new Error('API GET request failed: ' + response.status);
  }
  return response.json();
}

// Generic POST request
async function apiPost(endpoint, data) {
  const response = await fetch(API_BASE_URL + endpoint, {
    method: 'POST',
    headers: getHeaders(),
    body: JSON.stringify(data),
  });
  if (!response.ok) {
    throw new Error('API POST request failed: ' + response.status);
  }
  return response.json();
}

// Generic PUT request
async function apiPut(endpoint, data) {
  const response = await fetch(API_BASE_URL + endpoint, {
    method: 'PUT',
    headers: getHeaders(),
    body: JSON.stringify(data),
  });
  if (!response.ok) {
    throw new Error('API PUT request failed: ' + response.status);
  }
  return response.json();
}

// Generic DELETE request
async function apiDelete(endpoint) {
  const response = await fetch(API_BASE_URL + endpoint, {
    method: 'DELETE',
    headers: getHeaders(),
  });
  if (!response.ok) {
    throw new Error('API DELETE request failed: ' + response.status);
  }
  return response;
}
