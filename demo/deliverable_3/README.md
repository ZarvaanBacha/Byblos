# Draft of Byblos' Mobile App

- [View this Plantuml online](https://www.plantuml.com/plantuml/uml/bLJVRzCm47xFNs6DXtM2Rk8L45hBc9Wqc3J67t1nBgcPVuJpkQ8gyBVZk7P8ro91N-BxlhiltyLtlVGCnC7egf8Ql1TNmPYDs5P2p4LFQWsCyVmidXsZP6nYz4lqZvqmo2lNpCuYy5pqeTPAnk2d41UduXOxj4qMUtqbJY-gwiOoKWiInHUF9BP9zbeHrk0nGMqzF18k2O5n8QKBbcVv-R3fyIpHsdNApiQp2poyklImQ8ooOZiH-eoqLX9p1Q5nwr7yE9P0Z_p11TARU-1ONB1hHsQM9SyvcdeXPF3i3DADRLrqaesyDxrs6qInFT9K8OkiHid5Py5u6RoowFrW8wErSnh13ZSY8vDBxl5RG3ywjuvLktcNNSt-M2hGKKk57Di7agibWPMhsTzjTaU-oDbvgxGkHQ4sAkKKUmICHJTQIxV_viTylhkwILm-qWGg9gkgEXNpV_vDvczmaljl5Nd-10PJf64IDC1urouLyIh5q3I4tgSp6b0w6eVQCu5aSO-MGK-4UoKVG_-WiXWX1x9Zg9L4wt4OwAWobANU9Z9bbYNGWNR4TfKvXod8nuEiuA731I6CUH_TMjbkafAM_zP9OELiGj0sUx4DfaQwQsCfaj-3bkux-c7fofjB4tkM3zWdPdVDfr_Y9oR_ytx7kyXpc7jC-du73US4Ah_eUuezDTC9pkCAEof7X5zHvh-NedpoyYGwIuyacQPsG4h2gxnONhojoWZlyMLxKqQPwBsCl6fF-FI-I0xR5Pdg4cqJZFuD)

- [Plantuml Class Diagram Basic](https://plantuml.com/class-diagram)

## Users:
|                                 | Possible Methods / Variables        | Descriptions                                                                        |
|---------------------------------|-------------------------------------|-------------------------------------------------------------------------------------|
| User                            | FirebaseUser fbUser                 | Represents a Firebase User entry                                                    |
|                                 | createAccount(Account.type)         | Create an account (not for Admin)                                                   |
|                                 | deleteAccount(Account.type)         | Delete an account (not for Admin)                                                   |
|                                 | login()                             | Can log in                                                                          |
|                                 | logout()                            | Can log off (Optional)                                                              |
|---------------------------------|-------------------------------------|-------------------------------------------------------------------------------------|
| Administrator can:              |                                     | (User)                                                                              |
|                                 | createService()                     | Create services (>= 3)                                                              |
|                                 | deleteAccount(User user)            | Delete employee (branches) accounts                                                 |
|                                 | deleteAccount(User user)            | Delete customers accounts                                                           |
|                                 |                                     |                                                                                     |
| Set up:                         | setHourlyRate(Service service)      | the hourly rate (price)                                                             |
|                                 | setForm(Service.type, customerInfo) | ? customer info (when they request a service):                                      |
|---------------------------------|-------------------------------------|-------------------------------------------------------------------------------------|
| The Byblos branch employee can: |                                     | (User)                                                                              |
|                                 | createAccount(Employee)             | Create an account (aka branch account)                                              |
|                                 | selectService()                     | Select services provided (from the set of all available services)                   |
|                                 | setBusinessHour()                   | Enter the working hours of the branch                                               |
|                                 | boolean processServiceRequest()     | View submitted `Service Requests`                                                   |
|                                 |                                     | - Approve request                                                                   |
|                                 |                                     | - Reject request                                                                    |
|                                 | notifyCustomer()                    | Notify customer when the request was approved                                       |
|---------------------------------|-------------------------------------|-------------------------------------------------------------------------------------|
| The customer can:               |                                     | (User)                                                                              |
|                                 | createAccount(Customer)             | Create an account                                                                   |
|                                 | searchBranch()                      | input: `Service.address`, `Service.type`; output: available Services, working hours |
|                                 | selectService()                     | Select a service they would like to purchase                                        |
|                                 | getForm(Service.type).fill()        | Fill in the required information                                                    |
|                                 | submitForm()                        | Submit the `Service Requests`                                                       |
|                                 | rateBranch()                        | Rate their experience with the Byblos branch                                        |
|---------------------------------|-------------------------------------|-------------------------------------------------------------------------------------|

## Customer info Forms (for Admin):

|                   | Possible Methods / Variables | Descriptions                                  |
|-------------------|------------------------------|-----------------------------------------------|
| General Form      | firstName                    | first name                                    |
|                   | lastName                     | last name                                     |
|                   | dateOfBirth                  | date of birth                                 |
|                   | address                      | address                                       |
|                   | email                        | email                                         |
|-------------------|------------------------------|-----------------------------------------------|
| Rental Form       |                              | (General Form)                                |
|                   | pickupTime                   | pickup date and time                          |
|                   | returnTime                   | return date and time                          |
|                   | licenseType                  | license type (G1 G2 G)                        |
|-------------------|------------------------------|-----------------------------------------------|
|-------------------|------------------------------|-----------------------------------------------|
| Car rental        |                              | (Rental Form)                                 |
|                   | carType                      | preferred car type (compact intermediate SUV) |
|-------------------|------------------------------|-----------------------------------------------|
| Truck rental      |                              | (Rental Form)                                 |
|                   | usageArea                    | the area where the truck will be used         |
|-------------------|------------------------------|-----------------------------------------------|
| Moving assistance |                              | (General Form)                                |
|                   | startLocation                | moving start location                         |
|                   | endLocation                  | moving end location                           |
|                   | numberOfMovers               | number of movers required                     |
|                   | numberOfBoxes                | expected number of boxes                      |
|-------------------|------------------------------|-----------------------------------------------|

## Services:

|               | Possible Methods / Variables | Descriptions                                                              |
|---------------|------------------------------|---------------------------------------------------------------------------|
| Basic Service | Form formInfo                | the Service formInfo, the main container customers' information, requests |
|               | type                         | can be expanded as needed: carRental; truckRental; movingAssistance       |
|               | hourlyRate                   | is set only by the Admin                                                  |
|               | isApproved                   | whether is approved (set by Employee)                                     |
|               | getForm()                    | get Customer info formInfo                                                |
|               | approve()                    | set isApproved to false                                                   |
|               | reject()                     | set isApproved to true                                                    |
|---------------|------------------------------|---------------------------------------------------------------------------|


---
## Final Check:

| The application should:                                       |
|---------------------------------------------------------------|
| Byblos branch can display a summary of all `Service Requests` |
| Show the rating of each branch                                |
| Show admin account with all registered users                  |
| Allow admin to delete user accounts                           |


---
### Deliverable 3: Employee Functionalities
