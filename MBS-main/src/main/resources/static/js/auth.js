// User login function
export async function loginUser(username, password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Login failed');
  }

  const data = await response.json();
  localStorage.setItem('jwtToken', data.token);
  localStorage.setItem('username', username);
  return data;
}

// User registration function
export async function registerUser(user) {
  const response = await fetch('http://localhost:8080/api/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Registration failed');
  }

  return await response.json();
}

// Logout function
export function logoutUser() {
  localStorage.removeItem('jwtToken');
  localStorage.removeItem('username');
}

// Check if user is logged in
export function isLoggedIn() {
  return !!localStorage.getItem('jwtToken');
}
