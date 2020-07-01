import React from 'react'
import PropTypes from 'prop-types'

const ProductItem = ({ product, onAddToCartClicked }) => (
  //<div style={{ marginBottom: 20 }}>
  <tr>
    <td>
      {product.title}-{product.price}-{product.inventory ? ` x${product.inventory}` : null}
    </td>
    <td>
      <button
        onClick={onAddToCartClicked}
        disabled={product.inventory > 0 ? '' : 'disabled'}>
        {product.inventory > 0 ? 'Add to cart' : 'Sold Out'}
      </button>
    </td>
  </tr>
  //</div>
)

ProductItem.propTypes = {
  product: PropTypes.shape({
    title: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired,
    inventory: PropTypes.number.isRequired
  }).isRequired,
  onAddToCartClicked: PropTypes.func.isRequired
}

export default ProductItem
