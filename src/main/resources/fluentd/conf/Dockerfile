FROM fluent/fluentd:v1.14-1

# Switch to root user to install dependencies
USER root

# Install dependencies for building Ruby from source, including OpenSSL
RUN apk update && apk add --no-cache \
    build-base \
    libffi-dev \
    libssl1.1 \
    zlib-dev \
    git \
    openssl-dev \
    && apk add --no-cache --virtual .build-deps \
    bison \
    readline-dev \
    yaml-dev \
    && wget https://cache.ruby-lang.org/pub/ruby/3.1/ruby-3.1.0.tar.gz \
    && tar -xzvf ruby-3.1.0.tar.gz \
    && cd ruby-3.1.0 \
    && ./configure --disable-install-doc \
    && make \
    && make install \
    && cd .. \
    && rm -rf ruby-3.1.0.tar.gz ruby-3.1.0

# Remove build dependencies
RUN apk del .build-deps

# Ensure the system uses the new Ruby version
RUN ln -sf /usr/local/bin/ruby /usr/bin/ruby \
    && ln -sf /usr/local/bin/gem /usr/bin/gem \
    && ln -sf /usr/local/bin/bundle /usr/bin/bundle

# Verify Ruby version after installation
RUN ruby -v

# Update RubyGems system
RUN gem update --system && gem pristine --all

# Reinstall Fluentd using Ruby 3.1
RUN gem install fluentd --no-document

# Reinstall Fluentd Plugin Manager
RUN gem install fluent-plugin-elasticsearch --no-document

# Verify Fluentd installation
RUN fluentd --version

# Switch back to the fluentd user
USER fluent

# Copy the Fluentd config file
COPY ./fluentd.conf /fluentd/etc/fluent.conf

# Expose the necessary ports
EXPOSE 24224
