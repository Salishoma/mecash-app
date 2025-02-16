## MeCash App


mecash is a fintech app that allows users, merchants as well as organizations to transfer and receive money. Transferring money to a different currency can be very stressful and time consuming, as one would have to use a second app thereby facing some bottlenecks as well as extra charges. With mecash, you have nothing to worry about as we got you covered. Transferring money to other currency has never been made easier than this as all you need to do is just a click away and your money is transferred to the other account's currency.

## Technology
- **Programming Language**: Java
- **Framework**: Spring Boot
- **ORM**: JPA/Hibernate
- **Database**: Relational Database (PostgreSQL)
- **Testing**: JUnit, Mockito
- **Version Control**: Git
- **Authentication**: JWT (JSON Web Token)
- **API Documentation**: Swagger UI


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
- **POST** */api/users/generate-access-token* - Login endpoint for users. Access token is generated on successful login and this token is used to access other endpoints opened to the user. Throws Unauthorized exception for wrong credentials.

## Pin Operation
- **POST** */api/users/create-pin* - Logged in user can use this endpoint to create their transaction pin which will be used by the user for any transaction to be done within the app. Throws UserNotFoundException when user does not exist, AuthenticateUserException for unauthorized users.
- **POST** */api/users/update-pin* - Logged in user can use this endpoint to update their transaction pin. Throws TransactionPinException for wrong pin, UserNotFoundException when user does not exist, AuthenticateUserException for unauthorized users.


## Wallet Operations
- **POST** */api/transactions/deposit* - Make deposit to wallet. Throws InvalidAmountException for amount less than 0.
- **POST** */api/transactions/transfer* - Transfer money from one wallet account to another. Throws AuthenticateUserException for unauthorized users, InvalidAmountException for amount less than 0 and when the wallet balance is less than transaction amount.
- **GET** */api/transactions/transaction-history* - To view all transactions done by the wallet owner.Throws AuthenticateUserException for unauthorized users.
- **POST** */api/wallets/account* - To create wallet.Throws AuthenticateUserException for unauthorized users.
- **GET** */api/wallets/account* - To get wallet.Throws AuthenticateUserException for unauthorized users.