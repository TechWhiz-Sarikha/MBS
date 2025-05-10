let selectedSeats = [];

async function initSeatSelection() {
  const params = new URLSearchParams(window.location.search);
  const showtimeId = params.get('showtimeId');
  if (!showtimeId) {
    alert('No showtime ID provided');
    return;
  }

  try {
    // Fetch seat layout and booked seats for the showtime
    const seats = await apiGet(`/showtimes/${showtimeId}/seats`);
    const bookedSeatIds = await apiGet(`/showtimes/${showtimeId}/bookedSeats`);

    const seatMap = document.getElementById('seat-map');
    seatMap.innerHTML = '';

    seats.forEach(seat => {
      const seatDiv = document.createElement('div');
      seatDiv.className = 'seat';
      seatDiv.textContent = seat.seatNumber;

      if (bookedSeatIds.includes(seat.id)) {
        seatDiv.classList.add('occupied');
      } else {
        seatDiv.addEventListener('click', () => toggleSeatSelection(seat, seatDiv));
      }

      seatMap.appendChild(seatDiv);
    });

    document.getElementById('confirm-booking-btn').addEventListener('click', () => confirmBooking(showtimeId));
  } catch (error) {
    console.error('Failed to load seats:', error);
  }
}

function toggleSeatSelection(seat, seatDiv) {
  const index = selectedSeats.findIndex(s => s.id === seat.id);
  if (index > -1) {
    selectedSeats.splice(index, 1);
    seatDiv.classList.remove('selected');
  } else {
    selectedSeats.push(seat);
    seatDiv.classList.add('selected');
  }
  updateSelectedSeatsDisplay();
  updateConfirmButtonState();
}

function updateSelectedSeatsDisplay() {
  const selectedSeatsSpan = document.getElementById('selected-seats');
  selectedSeatsSpan.textContent = selectedSeats.map(s => s.seatNumber).join(', ') || 'None';
}

function updateConfirmButtonState() {
  const btn = document.getElementById('confirm-booking-btn');
  btn.disabled = selectedSeats.length === 0;
}

async function confirmBooking(showtimeId) {
  const userId = localStorage.getItem('userId');
  if (!userId) {
    alert('Please login to book seats.');
    window.location.href = 'login.html';
    return;
  }

  const seatIds = selectedSeats.map(s => s.id);
  const bookingData = {
    userId: parseInt(userId),
    showtimeId: parseInt(showtimeId),
    numTickets: seatIds.length,
    selectedSeatIds: seatIds.join(',')
  };

  try {
    await apiPost('/bookings', bookingData);
    alert('Booking confirmed!');
    window.location.href = 'user-dashboard.html';
  } catch (error) {
    document.getElementById('booking-error').textContent = 'Booking failed: ' + error.message;
  }
}
