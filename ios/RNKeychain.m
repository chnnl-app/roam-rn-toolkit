#import "RNKeychain.h"

@implementation RNKeychain (Bridge)

RCT_EXPORT_MODULE_NO_LOAD(RNKeychain, RNKeychain)

+ (BOOL)requiresMainQueueSetup {
    return true;
}

RCT_EXTERN_METHOD(getValueForKey: (NSString *) key
                  resolver: (RCTPromiseResolveBlock) resolver
                  rejector: (RCTPromiseRejectBlock) rejector)

RCT_EXTERN_METHOD(setValueForKey: (NSString *) key
                  value: (NSString *) value
                  resolver: (RCTPromiseResolveBlock) resolver
                  rejector: (RCTPromiseRejectBlock) rejector)

RCT_EXTERN_METHOD(deleteValueForKey: (NSString *) key
                  resolver: (RCTPromiseResolveBlock) resolver
                  rejector: (RCTPromiseRejectBlock) rejector)

RCT_EXTERN_METHOD(generateKeyWithLength: (NSInteger) length
                  resolver: (RCTPromiseResolveBlock) resolver
                  rejector: (RCTPromiseRejectBlock) rejector)

@end
