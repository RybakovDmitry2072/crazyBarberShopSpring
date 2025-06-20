$(document).ready(function() {
    loadEmployees(0);
});

function loadEmployees(page) {
    $.ajax({
        url: '/api/employees',
        method: 'GET',
        data: {
            page: page,
            size: 6,
            role: 'BARBER'
        },
        success: function(data) {
            const $container = $('.t532__container');
            $container.empty();

            if (data.content.length === 0) {
                $container.html(`
                    <div class="no-barbers-message" style="display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 300px; margin: 40px 0; padding: 40px; background-color: #f8f9fa; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); width: 100%;">
                        <h4 style="font-size: 24px; color: #444; margin-bottom: 15px;">Нет доступных барберов</h4>
                    </div>
                `);
                return;
            }

            data.content.forEach(function(employee) {
                const employeeHtml = createEmployeeCard(employee);
                $container.append(employeeHtml);
            });

            if (data.totalPages > 1) {
                createPagination(data.totalPages, data.number);
            }
        },
        error: function(xhr, status, error) {
            console.error('Ошибка при загрузке сотрудников:', error);

            const $container = $('.t532__container');
            $container.empty();

            let message = 'Ошибка при загрузке сотрудников. Попробуйте позже.';

            if (xhr.responseJSON && xhr.responseJSON.message) {
                message = xhr.responseJSON.message;
            }

            $container.html(`
                <div class="t532__error-message" style="text-align:center; padding: 30px;">
                    <p style="font-size: 18px; color: red;">${message}</p>
                </div>
            `);
        }
    });
}

function createEmployeeCard(employee) {
    const imageUrl = employee.image ? `/images/employees/${employee.image.storageFileName}` : '/images/employees/Frame_67.png';
    
    return `
        <div class="t532__col t-col t-col_4 t-align_left t532__show_hover">
            <div class="t532__itemwrapper" style="height: 450px;">
                <div class="t532__bg t532__animation_slow" 
                     style="background-image: url('${imageUrl}');">
                    </div>
                    <div class="t532__overlay t532__animation_slow"></div>
                    <div class="t532__textwrapper t532__animation_slow">
                        <div class="t532__textwrapper__content">
                            <div class="t532__title t-name t-name_lg">${employee.firstName} ${employee.lastName}</div>
                            <div class="t532__descr t-descr t-descr_xs">Опыт работы: ${employee.experienceYears} лет</div>
                        </div>
                    </div>
                </div>
            </div>
    `;
}

function createPagination(totalPages, currentPage) {
    const $paginationContainer = $('<div/>', {
        class: 'pagination',
        css: {
            'text-align': 'center',
            'margin-top': '30px'
        }
    });

    for (let i = 0; i < totalPages; i++) {
        const $button = $('<button/>', {
            class: `page-button ${i === currentPage ? 'active' : ''}`,
            text: i + 1,
            click: function() {
                loadEmployees(i);
            },
            css: {
                'margin': '0 5px',
                'padding': '5px 10px',
                'border': '1px solid #000',
                'background': i === currentPage ? '#000' : '#fff',
                'color': i === currentPage ? '#fff' : '#000',
                'cursor': 'pointer'
            }
        });
        
        $paginationContainer.append($button);
    }

    $('.pagination').remove();
    $('.t532').append($paginationContainer);
} 