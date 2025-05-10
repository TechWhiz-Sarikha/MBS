async function loadMovieDetails() {
  const params = new URLSearchParams(window.location.search);
  const movieId = params.get('id');
  if (!movieId) {
    alert('No movie ID provided');
    return;
  }

  try {
    const movie = await apiGet('/movies/' + movieId);
    document.getElementById('movie-title').textContent = movie.title;
    document.getElementById('movie-poster').src = movie.posterUrl || '../assets/images/default-movie.png';
    document.getElementById('movie-poster').alt = movie.title;
    document.getElementById('movie-description').textContent = movie.description || 'No description available.';
    document.getElementById('movie-duration').textContent = movie.duration || 'N/A';

    const showtimes = await apiGet(`/movies/${movieId}/showtimes`);
    const showtimesList = document.getElementById('showtimes-list');
    showtimesList.innerHTML = '';

    if (showtimes.length === 0) {
      showtimesList.textContent = 'No showtimes available.';
      return;
    }

    showtimes.forEach(showtime => {
      const div = document.createElement('div');
      div.className = 'showtime-item';
      div.textContent = new Date(showtime.startTime).toLocaleString();
      div.addEventListener('click', () => {
        window.location.href = `seat-selection.html?showtimeId=${showtime.id}`;
      });
      showtimesList.appendChild(div);
    });
  } catch (error) {
    console.error('Failed to load movie details:', error);
  }
}
