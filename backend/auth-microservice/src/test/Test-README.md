**Author:** Darrick

This is how I've pictured our test structure to be after conducting some research on the topic.
I have referenced this video heavily: https://www.youtube.com/watch?v=aEW8ZH6wj2o

_(Applicable to tests in other microservices)_ 

**Note: Please refactor all test files accordingly**

Tests should be named as `<Entity><Type of Test>`Test (e.g. `DoctorUnitTest`, `PatientIntegrationTest`, etc.)

### Unit Tests
We will be testing only the **services** through the use of their respective mock repositories 
(look up **Mockito** for more details). This will **not** involve the controller at all.

Services to be tested in this microservice include (and excluding any appointment related methods):
- DoctorService
- PatientService (excluding medicine prescriptions; this will be in a separate microservice ideally)
- SpecialtyService
- AuthService (authentication-related tests go here)
- and any more as we progress if needed...

### Integration Tests
We will be mocking this entire microservice application and testing each endpoint through the use of
**Mockito**, Spring annotations **`@WebMvcTest`** and `@MockBean`, and `MockMvc` object. `MockMvc` is used to simulate
HTTP requests (GET, POST, etc.) to our API endpoints. In this test, we will still be using **Mockito** to mock 
the service this time.

The main focus is to test the controller endpoints with mock services.

### Acceptance Tests
This is very similar to integration tests, however we will no longer mock the service. We will mock the repository. 
Therefore, the main focus here is still to test the controller endpoints now with the actual services and 
mock repositories.

This section will involve a lot of referencing to the Acceptance Criterias that we have formulated and posted on
Jira.