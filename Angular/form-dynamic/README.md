
Example from https://danielk.tech/home/how-to-build-a-dynamic-angular-form
It is modified to work with Angular 11/12. 
Important: do not enable strict type when create the project (i.e. ng new <proj>)
because it uses assignments of generic type (i.e. class<T>)

Components:
- Definitions of all the fields (textbox, checkbox, combobox, radio, and validation)
- A dynamic form which takes the form fields, call a service to create a FormGroup, 
then display the FormGroup with a submit button.
- A Service to do 2 things: 
A. Create the form fields with static test data. In production, it should make a REST call
and populate the form fields array.
B. Transform the form fields array to a FormGroup

TODO:
- Add scss style. Need a design theme like bootstrap or something.
- Add retrieving data from REST calls
- Learn how it validates email (Angular built-in feature?)
- More test data (i.e. more realistic form fields)