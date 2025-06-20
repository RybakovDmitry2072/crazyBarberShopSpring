$(document).ready(function() {
    // Показываем состояние загрузки
    $('#loadingState').show();
    $('#noAppointments').hide();
    $('#appointmentsContainer').hide();
    
    loadEmployees();
});

function loadEmployees() {
    $.ajax({
        url: '/api/employees',
        method: 'GET',
        data: {
            page: 0,
            size: 10,
            role: 'BARBER'
        },
        success: function(data) {
            const $container = $('#appointmentsContainer');

            $('#loadingState').hide();

            if (!data || !data.content || data.content.length === 0) {
                $('#noAppointments').show();
                return;
            }

            $container.empty();
            data.content.forEach(function(employee) {
                const appointmentCard = createAppointmentCard(employee);
                $container.append(appointmentCard);

                loadTimeSlots(employee.id);
                loadHaircutCategories(employee.id);
            });

            $container.show();
        },
        error: function(xhr, status, error) {
            $('#loadingState').hide();
            console.error('Ошибка при загрузке барберов:', xhr);
            
            const $container = $('#appointmentsContainer');
            $container.empty().html(`
                <div class="error-message" style="text-align: center; padding: 20px; color: #dc3545;">
                    <i class="fas fa-exclamation-circle" style="font-size: 48px; margin-bottom: 20px;"></i>
                    <h4>Ошибка загрузки</h4>
                    <p>Не удалось загрузить список барберов. Пожалуйста, попробуйте позже.</p>
                </div>
            `);
            $container.show();
        }
    });
}

function createAppointmentCard(employee) {
    const imageUrl = employee.image ? `/images/employees/${employee.image.storageFileName}` : '/images/employees/Frame_67.png';

    const card = $(`
        <div class="appointment-card" data-employee-id="${employee.id}">
            <div class="barber-info">
                <img src="${imageUrl}" alt="${employee.firstName}" class="barber-photo">
                <h3>${employee.firstName} ${employee.lastName}</h3>
                <p>Опыт работы: ${employee.experienceYears} лет</p>
            </div>
            <div class="time-slots-container" id="timeSlots-${employee.id}">
                <h4>Доступное время</h4>
                <div class="time-slots"></div>
            </div>
            <div class="appointment-form">
                <select class="haircut-category" id="category-${employee.id}">
                    <option value="">Выберите услугу</option>
                </select>
                <button class="book-button" data-employee-id="${employee.id}" disabled>
                    Записаться
                </button>
            </div>
        </div>
    `);

    card.find('.book-button').click(function() {
        const employeeId = $(this).data('employee-id');
        bookAppointment(employeeId);
    });

    return card;
}

function loadTimeSlots(employeeId) {
    const now = new Date();
    const endDate = new Date();
    endDate.setDate(now.getDate() + 7);

    const $timeSlotsContainer = $(`#timeSlots-${employeeId} .time-slots`);
    $timeSlotsContainer.html('<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Загрузка времени...</div>');

    const formatDateForConverter = (date) => {
        return String(date.getDate()).padStart(2, '0') + '.' +
               String(date.getMonth() + 1).padStart(2, '0') + '.' +
               date.getFullYear() + ' ' +
               String(date.getHours()).padStart(2, '0') + ':' +
               String(date.getMinutes()).padStart(2, '0');
    };

    $.ajax({
        url: '/api/timeslots',
        method: 'GET',
        data: {
            employeeId: employeeId,
            startAfter: formatDateForConverter(now),
            endBefore: formatDateForConverter(endDate)
        },
        success: function(timeSlots) {
            $timeSlotsContainer.empty();

            if (timeSlots && timeSlots.length > 0) {
                const availableSlots = timeSlots.filter(slot => {
                    const slotTime = new Date(slot.startTime);
                    return !slot.booked && slotTime > now && slotTime < endDate;
                });

                if (availableSlots.length > 0) {
                    const slotsByDay = groupSlotsByDay(availableSlots);

                    for (const [day, slots] of Object.entries(slotsByDay)) {
                        const dayContainer = $(`
                            <div class="day-container mb-3">
                                <div class="day-header">
                                    <h5>${formatFullDate(day)}</h5>
                                </div>
                                <div class="time-slots-list"></div>
                            </div>
                        `);

                        const slotsContainer = dayContainer.find('.time-slots-list');

                        slots.forEach(slot => {
                            const timeButton = $(`
                                <button class="time-slot-button" data-slot-id="${slot.id}">
                                    ${formatTime(slot.startTime)}
                                </button>
                            `);

                            timeButton.click(function() {
                                $(`#timeSlots-${employeeId} .time-slot-button`).removeClass('selected');
                                $(this).addClass('selected');
                                checkBookingAvailability(employeeId);
                            });

                            slotsContainer.append(timeButton);
                        });

                        $timeSlotsContainer.append(dayContainer);
                    }
                } else {
                    $timeSlotsContainer.html('<p class="no-slots">Нет доступных временных слотов</p>');
                }
            } else {
                $timeSlotsContainer.html('<p class="no-slots">Нет доступных временных слотов</p>');
            }
        },
        error: function(error) {
            console.error('Ошибка при загрузке временных слотов:', error);
            $timeSlotsContainer.html(`
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i>
                    <p>Ошибка при загрузке временных слотов</p>
                </div>
            `);
        }
    });
}

function loadHaircutCategories(employeeId) {
    const $select = $(`#category-${employeeId}`);
    $select.prop('disabled', true).html('<option value="">Загрузка услуг...</option>');

    $.ajax({
        url: '/api/haircut-categories',
        method: 'GET',
        success: function(response) {
            $select.empty().append('<option value="">Выберите услугу</option>');

            const categories = response.content ? response.content : response;

            if (categories && categories.length > 0) {
                categories.forEach(category => {
                    $select.append(new Option(
                        `${category.name} - ${category.price}₽`,
                        category.id
                    ));
                });
            } else {
                $select.append(new Option('Нет доступных услуг', ''));
            }

            $select.prop('disabled', false).off('change').change(function() {
                checkBookingAvailability(employeeId);
            });
        },
        error: function(error) {
            console.error('Ошибка при загрузке категорий:', error);
            $select.empty()
                  .append(new Option('Ошибка загрузки услуг', ''))
                  .prop('disabled', true);
        }
    });
}

function checkBookingAvailability(employeeId) {
    const timeSlotSelected = $(`#timeSlots-${employeeId} .time-slot-button.selected`).length > 0;
    const categorySelected = $(`#category-${employeeId}`).val() !== '';

    $(`.appointment-card[data-employee-id="${employeeId}"] .book-button`)
        .prop('disabled', !(timeSlotSelected && categorySelected));
}

function bookAppointment(employeeId) {
    const timeSlotId = $(`#timeSlots-${employeeId} .time-slot-button.selected`).data('slot-id');
    const categoryId = $(`#category-${employeeId}`).val();
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    if (!timeSlotId || !categoryId) {
        showErrorMessage('Пожалуйста, выберите время и услугу');
        return;
    }

    const appointmentData = {
        employeeId: employeeId,
        timeSlotId: timeSlotId,
        haircutCategoryId: categoryId,
        notes: ''
    };

    console.log('Отправляемые данные:', appointmentData);

    $.ajax({
        url: '/api/appointments',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(appointmentData),
        headers: {
            [csrfHeader]: csrfToken
        },
        success: function(response) {
            console.log('Успешный ответ:', response);
            showSuccessMessage('Запись успешно создана!');
            setTimeout(() => {
                window.location.href = "/";
            }, 1000);
        },
        error: function(error) {
            console.error('Ошибка сервера:', error);
            let errorMessage = 'Ошибка при создании записи';
            if (error.responseJSON && error.responseJSON.message) {
                errorMessage += ': ' + error.responseJSON.message;
            }
            showErrorMessage(errorMessage);
        }
    });
}

function formatTime(dateString) {
    if (!dateString) return '';
    
    try {
        const parts = dateString.split(/[\s,T]/);
        if (parts.length >= 2) {
            const timePart = parts[1];
            const [hours, minutes] = timePart.split(':');
            return `${hours}:${minutes}`;
        }
        return dateString;
    } catch (e) {
        console.error('Error formatting time:', e);
        return dateString;
    }
}

function formatFullDate(dateString) {
    const [datePart] = dateString.split(' ');
    const [year, month, day] = datePart.split('-');
    const date = new Date(year, month - 1, day);

    const weekdays = {
        1: 'Пн',
        2: 'Вт',
        3: 'Ср',
        4: 'Чт',
        5: 'Пт',
        6: 'Сб',
        0: 'Вс'
    };
    
    const months = {
        0: 'янв',
        1: 'фев',
        2: 'мар',
        3: 'апр',
        4: 'мая',
        5: 'июн',
        6: 'июл',
        7: 'авг',
        8: 'сен',
        9: 'окт',
        10: 'ноя',
        11: 'дек'
    };
    
    const weekday = weekdays[date.getDay()];
    const dayNum = parseInt(day);
    const monthName = months[date.getMonth()];
    
    return `${weekday}, ${dayNum} ${monthName}`;
}

function groupSlotsByDay(slots) {
    const grouped = slots.reduce((acc, slot) => {
        try {
            let datePart;
            if (slot.startTime.includes('T')) {
                datePart = slot.startTime.split('T')[0];
            } else {
                datePart = slot.startTime.split(' ')[0];
            }
            
            if (!acc[datePart]) {
                acc[datePart] = [];
            }
            acc[datePart].push(slot);
            acc[datePart].sort((a, b) => {
                const timeA = a.startTime.split(/[\s,T]/)[1];
                const timeB = b.startTime.split(/[\s,T]/)[1];
                return timeA.localeCompare(timeB);
            });
        } catch (e) {
            console.error('Error grouping slot:', e, slot);
        }
        return acc;
    }, {});

    const sortedDays = Object.keys(grouped).sort((a, b) => {
        const [yearA, monthA, dayA] = a.split('-').map(Number);
        const [yearB, monthB, dayB] = b.split('-').map(Number);
        const dateA = new Date(yearA, monthA - 1, dayA);
        const dateB = new Date(yearB, monthB - 1, dayB);
        
        const dayOfWeekA = (dateA.getDay() || 7) - 1;
        const dayOfWeekB = (dateB.getDay() || 7) - 1;
        
        if (Math.abs(dateA - dateB) < 7 * 24 * 60 * 60 * 1000) {
            return dayOfWeekA - dayOfWeekB;
        }
        
        return dateA - dateB;
    });

    const sortedGrouped = {};
    sortedDays.forEach(day => {
        sortedGrouped[day] = grouped[day];
    });

    return sortedGrouped;
}

function getWeekNumber(date) {
    const d = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    const dayNum = d.getUTCDay() || 7;
    d.setUTCDate(d.getUTCDate() + 4 - dayNum);
    const yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    return Math.ceil((((d - yearStart) / 86400000) + 1) / 7);
}

function showSuccessMessage(message) {
    alert(message);
}

function showErrorMessage(message) {
    alert(message);
}