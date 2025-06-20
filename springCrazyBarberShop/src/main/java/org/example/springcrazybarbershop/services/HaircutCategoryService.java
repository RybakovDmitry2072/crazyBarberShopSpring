package org.example.springcrazybarbershop.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.springcrazybarbershop.dto.HaircutCategoryDto;
import org.example.springcrazybarbershop.mappers.HaircutCategoryMapper;
import org.example.springcrazybarbershop.models.HaircutCategory;
import org.example.springcrazybarbershop.repositories.HaircutCategoryRepository;
import org.example.springcrazybarbershop.services.interfeces.IHaircutCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HaircutCategoryService implements IHaircutCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(HaircutCategoryService.class);

    private final HaircutCategoryRepository haircutCategoryRepository;
    private final HaircutCategoryMapper haircutCategoryMapper;

    @Override
    public Page<HaircutCategoryDto> getAllHaircutCategory(int page, int size) {
        try {
            logger.debug("=== Начало получения категорий в сервисе ===");
            logger.debug("Запрошена страница: {}, размер: {}", page, size);
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
            logger.debug("Создан Pageable объект: {}", pageable);
            
            Page<HaircutCategory> categoriesPage = haircutCategoryRepository.findAll(pageable);
            logger.debug("Получено записей из БД: {}", categoriesPage.getTotalElements());
            
            if (!categoriesPage.hasContent()) {
                logger.warn("В базе данных нет категорий стрижек");
            } else {
                logger.debug("Первая категория из БД: {}", categoriesPage.getContent().get(0));
            }
            
            Page<HaircutCategoryDto> result = categoriesPage.map(haircutCategoryMapper::toHaircutCategoryDto);
            logger.debug("Преобразовано в DTO: {} записей", result.getContent().size());
            
            logger.debug("=== Завершение получения категорий в сервисе ===");
            return result;
            
        } catch (Exception e) {
            logger.error("Ошибка при получении категорий в сервисе: ", e);
            throw e;
        }
    }

    @Override
    public List<HaircutCategory> findAll() {
        logger.debug("Получение всех категорий стрижек");
        return haircutCategoryRepository.findAll();
    }

    @Override
    @Transactional
    public HaircutCategory save(HaircutCategory category) {
        logger.debug("Сохранение категории стрижки: {}", category);
        return haircutCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Удаление категории стрижки с ID: {}", id);
        haircutCategoryRepository.deleteById(id);
    }

    @Override
    public Optional<HaircutCategory> findById(Long id) {
        logger.debug("Поиск категории стрижки по ID: {}", id);
        return haircutCategoryRepository.findById(id);
    }
}
