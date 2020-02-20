#import <Foundation/Foundation.h>
#import "RNRoamRnToolkit-Swift.h"

#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNKeychain (Bridge) <RCTBridgeModule>
@end
