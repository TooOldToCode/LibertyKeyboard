# Crypto primitives are referenced directly, so R8 can safely retain only the
# reachable Tink and Bouncy Castle classes. No reflection-based keep rules are
# required.
-dontwarn javax.annotation.Nullable
