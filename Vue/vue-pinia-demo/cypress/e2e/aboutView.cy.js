const PORT = process.env.PORT || 5173

describe('Pinia demo with counters', () => {

    beforeEach(() => {
        console.log(PORT)
        cy.visit(`http://localhost:${PORT}`)
        cy.wait(500) // wait for the JS to load
    })

    it('Reads the store', () => {
        cy.window()
            .its('counterStore.count')
            .should('equal', 20);
    })

    it('should add one', () => {
        // Get the pinia store directly off the window object
        cy.window()
            .its('counterStore.count')
            .should('equal', 20);
        // Click the increment button
        cy.get('[data-cy=add-one]').click();
        // Re-check store and it should now be incremented
        cy.window()
            .its('counterStore.count')
            .should('equal', 21);
    })

    it('should intercept and succeed', () => {
        cy.intercept('https://jsonplaceholder.typicode.com/posts/*', { fixture: 'aboutViewData.json' }).as('matchedUrl')
        // open the right page so we can get the data-cy
        cy.visit(`http://localhost:${PORT}/about`)
        cy.get('[data-cy=my-url]')
            .should('be.visible')
            .click()
        cy.wait('@matchedUrl').then((interception) => {
            assert.isNotNull(interception.response.body, '1st API call has data')
        })
        // cy.wait('@matchedUrl').should('have.property', 'response.statusCode', 200)
    })

    it('should call counterStore simple action', () => {
        let store = null
        cy.visit(`http://localhost:${PORT}`)
            .then(win => {
                console.log(win)
                const oe = win.counterStore.greeting()
                console.log(oe)
                // store = win.store.counterStore
            })
    })

})