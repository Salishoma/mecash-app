## MeCash App

Mecash is a fintech app that allows users, merchants as well as organizations to transfer and receive money. Transferring money to a different currency can be very stressful and time consuming, as one would have to use a second app thereby facing some bottlenecks as well as extra charges. With mecash, you have nothing to worry about as we got you covered. Transferring money to other currency has never been made easier than this as all you need to do is just a click away and your money is transferred to the other account's currency.

## Technology
- **Programming Language**: Java
- **Framework**: Spring Boot
- **ORM**: JPA/Hibernate
- **Database**: Relational Database(PostgreSQL)
- **Testing**: JUnit, Mockito
- **Version Control**: Git
- **Authentication**: JWT (JSON Web Token)
- **API Documentation**: Swagger UI
- **DB Migration Tool**: Liquibase


## Services
- **DB migration service**: Responsible for creating of tables. To ensure your tables are ready, this is the first service to start.
- **Security Service**: This service is responsible for the authentication and authorization of the app. It is a centralized service for the security of the other services.
- **User Service** - This service is where user registers account, create pin etc.
- **Wallet Service** - This services is responsible for users transactions such as payments, transfers, getting transaction history etc.


## API Endpoints
Below are the key endpoints for interacting with the meCash API:

### Authentication
- **POST** */api/users/account* - Register a new account. Throws UserExistsException when account has already been taken.
- **PUT** */api/users/account* - Update an already existing user. Throws UserNotFoundException when user does not exist, AuthenticateUserException for unauthorized users.
- **GET** */api/users/account* - Get User's account. Throws UserNotFoundException when user does not exist, AuthenticateUserException for unauthorized users.
- **POST** */api/users//auth/login* - Login endpoint for users. Access token is generated on successful login and this token is used to access other endpoints opened to the user. Throws Unauthorized exception for wrong credentials.

## Pin Operation
- **POST** */api/users/create-pin* - Logged in user can use this endpoint to create their transaction pin which will be used by the user for any transaction to be done within the app. Throws UserNotFoundException when user does not exist, AuthenticateUserException for unauthorized users.
- **POST** */api/users/update-pin* - Logged in user can use this endpoint to update their transaction pin. Throws TransactionPinException for wrong pin, UserNotFoundException when user does not exist, AuthenticateUserException for unauthorized users.


## Wallet Operations
- **POST** */api/transactions/deposit* - Make deposit to wallet. Throws InvalidAmountException for amount less than 0.
- **POST** */api/transactions/transfer* - Transfer money from one wallet account to another. Throws AuthenticateUserException for unauthorized users, InvalidAmountException for amount less than 0 and when the wallet balance is less than transaction amount.
- **GET** */api/transactions/transaction-history* - To view all transactions done by the wallet owner.Throws AuthenticateUserException for unauthorized users.
- **POST** */api/wallets/account* - To create wallet.Throws AuthenticateUserException for unauthorized users.
- **GET** */api/wallets/account* - To get wallet.Throws AuthenticateUserException for unauthorized users.

## Application Swagger Docs

The swagger docs api for each module can be accessed via *<BASE_URL>/swagger-ui/index.html*


# Database Tables Structure
Below is the tabular form of all the tables and their constraints.

### Users Table
| Column Name       | Data Type  | Constraints                |
|------------------|-----------|----------------------------|
| id               | UUID      | Primary Key, Not Null      |
| first_name       | String    |                            |
| last_name        | String    |                            |
| email            | String    | Not Null, Unique           |
| phone_number     | String    |                            |
| user_name        | String    |                            |
| transaction_pin  | String    |                            |
| date_of_birth    | Date      |                            |
| account_type     | Enum      |                            |
| address          | Embedded  |                            |
| created_by       | String    |                            |
| created_on       | Timestamp |                            |
| last_modified_by | String    |                            |
| last_modified_on | Timestamp |                            |

### Wallets Table
| Column Name     | Data Type  | Constraints               |
|----------------|-----------|---------------------------|
| id             | UUID      | Primary Key, Not Null     |
| user_id        | UUID      | Foreign Key (Users.id)    |
| currency       | Enum      |                            |
| amount         | DECIMAL(19,2)    |                            |
| country        | String    | Default 'NG'              |
| account_type   | Enum      |                            |
| bank_name      | String    |                            |
| account_name   | String    |                            |
| account_number | String    |                            |

### Transaction Data Table
| Column Name                  | Data Type  | Constraints                     |
|------------------------------|-----------|---------------------------------|
| id                           | UUID      | Primary Key, Not Null           |
| currency                     | Enum      |                                 |
| sender_currency              | Enum      |                                 |
| receiver_currency            | Enum      |                                 |
| amount                       | DECIMAL(19,2)    |                                 |
| amount_in_sender_currency    | DECIMAL(19,2)    |                                 |
| amount_in_receiver_currency  | DECIMAL(19,2)    |                                 |
| sender                       | String    |                                 |
| account_name                 | String    |                                 |
| account_number               | String    |                                 |
| description                  | String    |                                 |
| reference_number             | String    |                                 |
| transaction_type             | Enum      |                                 |
| wallet_id                    | UUID      | Foreign Key (Wallets.id)        |
| user_id                      | UUID      | Foreign Key (Users.id)          |

### Auth User Table
| Column Name       | Data Type  | Constraints                |
|------------------|-----------|----------------------------|
| id               | UUID      | Primary Key, Not Null      |
| email            | String    | Not Null, Unique           |
| password         | String    | Not Null                   |
| created_date     | Timestamp | Not Null, Auto Generated   |
| roles           | Set<Enum> | Not Null                   |
| user_id         | UUID      | Foreign Key (Users.id), Unique, Not Null |
| account_type     | Enum      |                            |
| transaction_pin  | String    |                            |

