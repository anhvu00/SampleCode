# vue-pinia-demo

This template should help get you started developing with Vue 3 in Vite.

# run with vite (slightly different)
npm run dev

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).

## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Anh's Notes
10/7/22 Friday

- A demo app to learn Vue 3, Vite, VisualCode shortcut keys to program Vue,
Cypress, Pinia
- There are 2 routes: Home and About
- Buttons to add/subtract 1 ==> About can access this count via Pinia state
- Try to add a popup but fail (See /train/vue/popup-demo)

## TODO
- Test run cypress (done Sat 10/8/22)
- Add a function(arg) to Pinia state store (done Sat 10/8/22)
- Use Cypress to call that function(arg) (done Sat 10/8/22)
- Add a button to aboutView.vue to get some url. Need to npm install axios
- Use Cypress to intercept that url

### Make a pinia store available to Cypress:
- main.js = import the store + expose it to Cypress
- cypress test.js = access the store + its function
(see https://github.com/itsalaidbacklife/cypress-pinia-composition)
- other examples can be read here: 
https://docs.cypress.io/examples/examples/recipes#Blogs
https://github.com/cypress-io/cypress-example-recipes

- Add axios (done 10/8/22)
- Add Cypress intercept a random url, return a fixture mock data (done 10/8/22)
- ? Can Cypress call pinia store functions?



## FYI
- npm run dev ==> launch on different port. How to set port here?
- https://www.cypress.io/blog/2019/01/22/when-can-the-test-click/