module.exports = {
  webpack: (config, { isServer }) => {
    // Avoid using fs (file system) modules for client rendering
    // https://github.com/zeit/next.js/issues/7755#issuecomment-508633125
    if (!isServer) {
      config.node = {
        fs: 'empty'
      }
    }
    config.module.rules.push({
      test: /\.(png|svg|eot|otf|ttf|woff|woff2)$/,
      use: {
        loader: 'url-loader',
        options: {
          limit: 8192,
          publicPath: '/_next/static/',
          outputPath: 'static/',
          name: '[name].[ext]',
        },
      },
    });
    return config;
  }
};
