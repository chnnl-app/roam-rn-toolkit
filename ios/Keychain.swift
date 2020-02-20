//
//  Keychain.swift
//  RNRoamRnToolkit
//
//  Created by Stuart Austin on 1/10/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

import Foundation
import Security

/// General class that encapsulates keychain functionality
@objc(RNKeychain) class Keychain: NSObject {
    /// The error domain used for errors passed back to js
    static let errorDomain = "com.roamltd.rn-keychain"

    /// The kSecAttrService used for the keychain
    let service = "com.roamltd.rn-keychain"


    /**
     *  Fetch a value out of the keychain
     *  - Parameters:
     *      - key: The key for the value to fetch
     *      - resolver: success callback
     *      - rejector: failure callback
     **/
    @objc func getValue(forKey key: String,
                        resolver: RCTPromiseResolveBlock,
                        rejector: RCTPromiseRejectBlock) {
        let keyData = Data(key.utf8)

        let query: [CFString: Any] = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: service,
            kSecAttrGeneric: keyData,
            kSecAttrAccount: keyData,
            kSecReturnData: kCFBooleanTrue,
            kSecMatchLimit: kSecMatchLimitOne
        ]

        var resultObject: AnyObject?
        let resultCode = withUnsafeMutablePointer(to: &resultObject) {
            SecItemCopyMatching(query as CFDictionary,
                                UnsafeMutablePointer($0))
        }

        if let error = NSError(osStatus: resultCode) {
            rejector(String(error.code), error.localizedDescription, error)
        } else {
            let resultString = (resultObject as? Data).flatMap {
                String(data: $0, encoding: .utf8)
            }

            if let resultString = resultString {
                resolver(resultString)
            } else {
                resolver(NSNull())
            }
        }
    }

    /**
     *  Set a value within the keychain.
     *  - Parameters:
     *      - key: The key to use
     *      - value: The value to set
     *      - resolver: success callback
     *      - rejector: failure callback
     **/
    @objc func setValue(forKey key: String,
                        value: String,
                        resolver: RCTPromiseResolveBlock,
                        rejector: RCTPromiseRejectBlock) {

        // Remove any existing value
        deleteValue(forKey: key, resolver: { _ in }, rejector: { _,_,_  in })

        let keyData = Data(key.utf8)

        let query: [CFString: Any] = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: service,
            kSecAttrGeneric: keyData,
            kSecAttrAccount: keyData,
            kSecValueData: Data(value.utf8),
            kSecAttrAccessible: kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
        ]

        let resultCode = SecItemAdd(query as CFDictionary, nil)

        if let error = NSError(osStatus: resultCode) {
            rejector(String(error.code), error.localizedDescription, error)
        } else {
            resolver(nil)
        }
    }

    /**
     *  Delete value within the keychain.
     *  - Parameters:
     *      - key: The key for the value to delete
     *      - resolver: success callback
     *      - rejector: failure callback
     **/
    @objc func deleteValue(forKey key: String,
                           resolver: RCTPromiseResolveBlock,
                           rejector: RCTPromiseRejectBlock) {
        let keyData = Data(key.utf8)

        let query: [CFString: Any] = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: service,
            kSecAttrGeneric: keyData,
            kSecAttrAccount: keyData
        ]

        let resultCode = SecItemDelete(query as CFDictionary)

        if let error = NSError(osStatus: resultCode) {
            rejector(String(error.code), error.localizedDescription, error)
        } else {
            resolver(nil)
        }
    }

    /**
     *  Generate a cryptographically secure random hexadecimal string with a given length
     *  - Parameters:
     *      - length: The length of the random string to generate
     *      - resolver: success callback
     *      - rejector: failure callback
     **/
    @objc func generateKey(withLength length: Int,
                           resolver: RCTPromiseResolveBlock,
                           rejector: RCTPromiseRejectBlock) {
        var keyData: Data = Data(repeating: 0, count: length)
        if Int32(errSecSuccess) != (keyData.withUnsafeMutableBytes {
            SecRandomCopyBytes(kSecRandomDefault, length, $0)
        }) {
            fatalError("Reading random bytes failed")
        }

        let result = keyData.hexEncodedString(options: .upperCase)
        resolver(result)
    }
}

private extension NSError {
    /// Create an NSError from an OSStatus code, which is what
    /// keychain APIs return.
    convenience init?(osStatus: OSStatus) {
        let ignoreCodes: Set<OSStatus> = [noErr, errSecItemNotFound]
        guard !ignoreCodes.contains(osStatus) else {
            return nil
        }

        let userInfo: [String: Any] = [
            NSLocalizedDescriptionKey: NSLocalizedString("A keychain error has occured",
                                                         comment: "Keychain error")
        ]

        self.init(domain: Keychain.errorDomain,
                  code: Int(osStatus),
                  userInfo: userInfo)
    }
}

extension Data {
    struct HexEncodingOptions: OptionSet {
        let rawValue: Int
        static let upperCase = HexEncodingOptions(rawValue: 1 << 0)
    }

    func hexEncodedString(options: HexEncodingOptions = []) -> String {
        let format = options.contains(.upperCase) ? "%02hhX" : "%02hhx"
        return map { String(format: format, $0) }.joined()
    }
}
