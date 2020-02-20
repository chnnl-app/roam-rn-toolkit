import { NativeModules } from 'react-native'
const { RNKeychain } = NativeModules

class Keychain {
  /** Fetch a value out of the keychain
   * @param {string} key - The key for the value to be fetched
   * @return {Promise} Promise to be resolve with the value or an error
   */
  getValue(key) {
    return RNKeychain.getValueForKey(key)
  }

  /** Set a value within the keychain
   * @param {string} key - The key to be used for the value
   * @param {string} value - The value to be stored
   * @return {Promise} Promise to be resolve with no value or an error
   */
  setValue(key, value) {
    return RNKeychain.setValueForKey(key, value)
  }

  /** Delete a value within the keychain
   * @param {string} key - The key for the value to be fetched
   * @return {Promise} Promise to be resolve with no value or an error
   */
  deleteValue(key) {
    return RNKeychain.deleteValueForKey(key)
  }

  /** Generate a cryptographically secure random hexadecimal string with a given length
   * @param {int} length - The length of the key to be created
   * @return {Promise} Promise to be resolve with no value or an error
   */
  generateKey(length) {
    return RNKeychain.generateKeyWithLength(length)
  }
}

export default new Keychain()