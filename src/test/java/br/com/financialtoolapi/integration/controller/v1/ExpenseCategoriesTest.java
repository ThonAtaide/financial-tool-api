package br.com.financialtoolapi.integration.controller.v1;

import br.com.financialtoolapi.application.domain.entities.ExpenseCategoryEntity;
import br.com.financialtoolapi.application.domain.repositories.ExpenseCategoryRepository;
import br.com.financialtoolapi.controller.errorhandler.ErrorResponse;
import br.com.financialtoolapi.factory.ExpenseCategoryFactory;
import br.com.financialtoolapi.integration.controller.AbstractApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExpenseCategoriesTest extends AbstractApiTest {

    private static final String EXPENSE_CATEGORIES_RESOURCE = "/expenseCategories";

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Test
    @DisplayName("Given that user has not sign in " +
            "When user try to get expense categories " +
            "Then api should return UNAUTHORIZED response")
    void testGetRequestIntoAllExpenseCategoriesWhenUserHasNotSignInYet() {
        populateExpenseCategoriesTable();
        final ResponseEntity<ErrorResponse> response = restTemplate
                .getForEntity(
                        concatServerUrlWithResourcePath(EXPENSE_CATEGORIES_RESOURCE),
                        ErrorResponse.class
                );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Given that user sign in successfully" +
            "When user try to get expense categories " +
            "Then api should return status code OK with paginated expense categories.")
    void testGetRequestIntoAllExpenseCategoriesWhenUserHasSignIn() {
        populateExpenseCategoriesTable();

        final HttpHeaders headers = signInRandomUserAndExtractAccessTokenHeaders();
        final ResponseEntity<LinkedHashMap> response = restTemplate
                .exchange(
                        concatServerUrlWithResourcePath(EXPENSE_CATEGORIES_RESOURCE),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        LinkedHashMap.class
                );

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final LinkedHashMap page = (LinkedHashMap) response.getBody().get("page");
        assertThat(page.get("totalElements")).isEqualTo(4);
    }

    @Test
    @DisplayName("Given that user has not sign in " +
            "When user try to get an specific expense category " +
            "Then api should return UNAUTHORIZED response")
    void testGetRequestOverSpecificExpenseCategoryWhenUserHasNotSignInYet() {
        populateExpenseCategoriesTable();
        final ResponseEntity<ErrorResponse> response = restTemplate
                .getForEntity(
                        concatServerUrlWithResourcePath(EXPENSE_CATEGORIES_RESOURCE.concat("/1")),
                        ErrorResponse.class
                );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Given that user sign in successfully" +
            "When user try to get specific expense category " +
            "Then api should return status code OK with expense category data.")
    void testGetRequestOverSpecificExpenseCategoryWhenUserHasSignIn() {
        populateExpenseCategoriesTable();

        final HttpHeaders headers = signInRandomUserAndExtractAccessTokenHeaders();
        final ResponseEntity<ExpenseCategoryEntity> response = restTemplate
                .exchange(
                        concatServerUrlWithResourcePath(EXPENSE_CATEGORIES_RESOURCE.concat("/1")),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        ExpenseCategoryEntity.class
                );

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1);
        assertThat(response.getBody().getName()).isNotNull();
    }

    @Test
    @DisplayName("Given that user sign in successfully" +
            "When user try to get specific expense category " +
            "Then api should return status code OK with expense category data.")
    void testPostRequestToCreateExpenseCategoryWhenUserHasSignIn() {
        final ExpenseCategoryEntity newCategory = ExpenseCategoryFactory.buildWith();
        final HttpHeaders headers = signInRandomUserAndExtractAccessTokenHeaders();
        final ResponseEntity<ErrorResponse> response = restTemplate
                .exchange(
                        concatServerUrlWithResourcePath(EXPENSE_CATEGORIES_RESOURCE),
                        HttpMethod.POST,
                        new HttpEntity<>(newCategory, headers),
                        ErrorResponse.class
                );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private void populateExpenseCategoriesTable() {
        expenseCategoryRepository.saveAll(List.of(
                ExpenseCategoryFactory.buildWith(),
                ExpenseCategoryFactory.buildWith(),
                ExpenseCategoryFactory.buildWith(),
                ExpenseCategoryFactory.buildWith()
        ));
    }

}
