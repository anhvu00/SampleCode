import React from 'react'
import PropTypes from 'prop-types'
import { Table } from 'react-bootstrap'

// children is ProductItem
// Table is react-bootstrap
const ProductsList = ({ title, children }) => (
  <div>
    <h3>{title}</h3>
    <Table striped bordered hover>
      <thead class="thead-dark">
        <tr>
          <th>column1</th>
          <th>column2</th>
        </tr>
      </thead>
      <tbody>
        {children}
      </tbody>
    </Table>
  </div>
)

ProductsList.propTypes = {
  children: PropTypes.node,
  title: PropTypes.string.isRequired
}

export default ProductsList
