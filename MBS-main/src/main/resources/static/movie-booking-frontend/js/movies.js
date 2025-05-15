async function loadFeaturedMovies() {
  try {
    const movies = await apiGet('/movies');
    const carousel = document.getElementById('featured-carousel');
    carousel.innerHTML = '';

    movies.forEach(movie => {
      const card = createMovieCard(movie);
      carousel.appendChild(card);
    });
  } catch (error) {
    console.error('Failed to load featured movies:', error);
  }
}

function createMovieCard(movie) {
  const card = document.createElement('div');
  card.className = 'movie-card';

  const img = document.createElement('img');
  img.src = movie.posterUrl || '../assets/images/default-movie.png';
  img.alt = movie.title;

  const info = document.createElement('div');
  info.className = 'movie-info';

  const title = document.createElement('h3');
  title.className = 'movie-title';
  title.textContent = movie.title;

  const desc = document.createElement('p');
  desc.className = 'movie-description';
  desc.textContent = movie.description || 'No description available.';

  info.appendChild(title);
  info.appendChild(desc);

  card.appendChild(img);
  card.appendChild(info);

  card.addEventListener('click', () => {
    window.location.href = `pages/movie-details.html?id=${movie.id}`;
  });

  return card;
}
