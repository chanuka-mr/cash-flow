package com.chanuka.cash_flow.service;

import com.chanuka.cash_flow.dto.CategoryDTO;
import com.chanuka.cash_flow.entity.CategoryEntity;
import com.chanuka.cash_flow.entity.ProfileEntity;
import com.chanuka.cash_flow.exception.CategoryAlreadyExistsException;
import com.chanuka.cash_flow.exception.CategoryNotFoundException;
import com.chanuka.cash_flow.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    // save Category
    public CategoryDTO saveCategory(@NonNull CategoryDTO categoryDTO) {
        ProfileEntity profileEntity = profileService.getCurrentProfile();

        // check if the category already exist
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profileEntity.getId())) {
            throw new CategoryAlreadyExistsException("Category already exists with the same name");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profileEntity);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    // get all categories for current user
    public List<CategoryDTO>  getCategoriesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    // get categories by type
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist or not accessible"));
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setIcon(categoryDTO.getIcon());
        existingCategory.setType(categoryDTO.getType());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }

    public void deleteCategory(Long categoryId) {
        ProfileEntity profile = profileService.getCurrentProfile();

        CategoryEntity category = categoryRepository
                .findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category does not exist or not accessible"
                ));
        categoryRepository.delete(category);
    }

    // helper method
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .type(categoryEntity.getType())
                .build();
    }
}
