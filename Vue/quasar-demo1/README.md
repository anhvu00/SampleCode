### Quasar demo. Anh Vu. 2/20/23
### PROJECT DESCRIPTION

Quasar v2
Vue 3 js (composite API)

Compile:
npm install

Run:
quasar dev
(localhost:8081/#/)

There are 3 pages (see routes.js)
1. localhost:8081/#/ = IndexPage.vue (It shows how a v-if toggle work)
2. localhost:8081/#/NewPage = NewPage.vue
3. localhost:8081/#/vif = ConditionalDisplay.vue (This is most interesting)

The ConditionalDisplay.vue shows a chain of popup using v-if and custom Dialog Component.
Requirement:
A. If input text is empty, show a "missing data" confirm dialog. If user accepted, continue.
B. If input text is "abc", show a "conflict data" confirm dialog. If user accepted, save form.

The above (A, B) conditions are on the same level and can be mutually exclusive.
So there are 4 possible outcome/tests for (A, B)
T, T
T, F
F, T
F, F

For example, input "abc" should not trigger "missing data" but must trigger "conflict data".
The take-away logic is that accepting the first popup will "emit" a value to the parent,
then the parent will check if a second popup is necessary.
There are other techniques to handle this such as:
- watcher (for validating input)
- async/await with Promise (for waiting until the user accept/reject on the popup)
- expose child component (the popup component) variables/functions to the parent
But I don't have time to research those options.
