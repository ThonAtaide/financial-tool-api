CREATE UNIQUE INDEX CONCURRENTLY CATEGORY_INDEX
    ON EXPENSE (CATEGORY_ID);

CREATE UNIQUE INDEX CONCURRENTLY EXPENSE_OWNER_INDEX
    ON EXPENSE (EXPENSE_OWNER);

CREATE UNIQUE INDEX CONCURRENTLY DAT_PURCHASE_INDEX
    ON EXPENSE (DAT_PURCHASE);

CREATE UNIQUE INDEX CONCURRENTLY IS_FIXED_INDEX
    ON EXPENSE (IS_FIXED_EXPENSE);